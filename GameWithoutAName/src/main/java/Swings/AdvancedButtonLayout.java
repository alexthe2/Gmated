/*
 * Copyright (c) 1995, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package Swings;

import config.ChessPoint;
import view.GridBox.GridButton;

import java.awt.*;
import java.util.HashMap;

public class AdvancedButtonLayout implements LayoutManager {

    private HashMap<ChessPoint, GridButton> buttons;

    private int width;
    private int height;
    private int res;


    public AdvancedButtonLayout(int width, int height, int res, HashMap<ChessPoint, GridButton> buttons)   {
        this.width = width;
        this.height = height;
        this.res = res;
        this.buttons = buttons;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        final Insets insets;
        synchronized (parent.getTreeLock()) {
            insets = parent.getInsets();
        }

        return new Dimension(width * res + insets.left + insets.right, height * res + insets.top + insets.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        final Insets insets;
        synchronized (parent.getTreeLock()) {
            insets = parent.getInsets();
        }

        return new Dimension(width * res + insets.left + insets.right, height * res + insets.top + insets.bottom);
    }

    @Override
    public void layoutContainer(Container parent) {
        final Insets insets;
        synchronized (parent.getTreeLock()) {
            insets = parent.getInsets();
        }

        for(final var pair : buttons.entrySet()) {
            final var point = pair.getKey();
            final var comp = pair.getValue();

            final var x = point.getX() * res;
            final var y = point.getY() * res;

            comp.setBounds(x, y, res, res);
            if(comp instanceof IHotRescale) {
                ((IHotRescale) comp).onRescale(res, res);
            }
        }
    }
}
