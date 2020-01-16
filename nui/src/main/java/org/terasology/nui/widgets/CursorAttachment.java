/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.nui.widgets;

import org.terasology.input.MouseInput;
import org.terasology.input.device.MouseDevice;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2i;
import org.terasology.nui.BaseInteractionListener;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.FocusManager;
import org.terasology.nui.InteractionListener;
import org.terasology.nui.LayoutConfig;
import org.terasology.nui.SubRegion;
import org.terasology.nui.UIWidget;
import org.terasology.nui.events.NUIMouseClickEvent;
import org.terasology.nui.events.NUIMouseDoubleClickEvent;
import org.terasology.nui.events.NUIMouseDragEvent;
import org.terasology.nui.events.NUIMouseOverEvent;
import org.terasology.nui.events.NUIMouseReleaseEvent;
import org.terasology.nui.events.NUIMouseWheelEvent;
import org.terasology.nui.skin.UIStyle;

/**
 */
public class CursorAttachment extends CoreWidget {
    private static final int MOUSE_CURSOR_HEIGHT = 18;

    @LayoutConfig
    private UIWidget attachment;

    private InteractionListener interactionListener = new BaseInteractionListener() {
        @Override
        public void onMouseOver(NUIMouseOverEvent event) {
            mouse = event.getMouse();
        }

        @Override
        public boolean isMouseOver() {
            // Do not block input from other elements
            return false;
        }
    };
    private MouseDevice mouse;

    public UIWidget getAttachment() {
        return attachment;
    }

    public void setAttachment(UIWidget attachment) {
        this.attachment = attachment;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isEnabled() && mouse == null) {
            canvas.addInteractionRegion(interactionListener, canvas.getRegion());
        }

        if (mouse == null) {
            return;
        }

        UIStyle style = canvas.getCurrentStyle();
        Vector2i attachmentSize = canvas.calculatePreferredSize(attachment);
        attachmentSize.add(style.getMargin().getTotals());

        int top;
        switch (style.getVerticalAlignment()) {
            case TOP:
                top = mouse.getPosition().y - attachmentSize.y;
                break;
            case MIDDLE:
                top = mouse.getPosition().y - attachmentSize.y / 2;
                break;
            default:
                top = mouse.getPosition().y + MOUSE_CURSOR_HEIGHT;
                break;
        }
        top = TeraMath.clamp(top, 0, canvas.size().y - attachmentSize.y);
        int left;
        switch (style.getHorizontalAlignment()) {
            case RIGHT:
                left = mouse.getPosition().x - attachmentSize.x;
                break;
            case CENTER:
                left = mouse.getPosition().x - attachmentSize.x / 2;
                break;
            default:
                left = mouse.getPosition().x;
                break;
        }
        left = TeraMath.clamp(left, 0, canvas.size().x - attachmentSize.x);


        try (SubRegion ignored = canvas.subRegion(Rect2i.createFromMinAndSize(left, top, attachmentSize.x, attachmentSize.y), false)) {
            canvas.drawBackground();
            canvas.drawWidget(attachment, style.getBackgroundBorder().shrink(canvas.getRegion()));
        }
    }

    @Override
    public boolean isSkinAppliedByCanvas() {
        return false;
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        return canvas.calculateRestrictedSize(attachment, sizeHint);
    }

    @Override
    public boolean isVisible() {
        // TODO: onDraw is never called unless isVisible returns true, so there is no reliable method of determining visibility for the moment.
        return super.isVisible() && (mouse == null || mouse.isVisible()) && getAttachment() != null && getAttachment().isVisible();
    }
}
