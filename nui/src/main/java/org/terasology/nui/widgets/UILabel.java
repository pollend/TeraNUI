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

import org.joml.Vector2i;
import org.terasology.nui.asset.font.Font;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.LayoutConfig;
import org.terasology.nui.TextLineBuilder;
import org.terasology.nui.databinding.Binding;
import org.terasology.nui.databinding.DefaultBinding;

import java.util.List;

/**
 * A (multi-line) label widget.
 */
public class UILabel extends CoreWidget {

    @LayoutConfig
    private Binding<String> text = new DefaultBinding<>("");

    public UILabel() {
    }

    public UILabel(String text) {
        this.text.set(text);
    }

    public UILabel(Binding<String> text) {
        this.text = text;
    }

    public UILabel(String id, String text) {
        super(id);
        this.text.set(text);
    }

    public UILabel(String id, String family, String text) {
        super(id);
        this.text.set(text);
        setFamily(family);
    }

    public UILabel(String id, Binding<String> text) {
        super(id);
        this.text = text;
    }

    /**
     * @return The text on the label.
     */
    public String getText() {
        if (text.get() == null) {
            return "";
        }
        return text.get();
    }

    @Override
    public String getMode() {
        if (isEnabled()) {
            return DEFAULT_MODE;
        }
        return DISABLED_MODE;
    }

    /**
     * @param text The new text to show.
     */
    public void setText(String text) {
        this.text.set(text);
    }

    public void bindText(Binding<String> binding) {
        this.text = binding;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(getText());
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i areaHint) {
        Font font = canvas.getCurrentStyle().getFont();
        List<String> lines = TextLineBuilder.getLines(font, getText(), areaHint.x);
        return font.getSize(lines);
    }
}
