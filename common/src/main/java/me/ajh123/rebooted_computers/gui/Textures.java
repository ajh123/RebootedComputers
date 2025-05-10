/*
 * This file contains code from Open Computers 2 which is licensed under the MIT.
 * This means the Rebooted Computers license does not apply to this file.
 *
 * See below for the full license:
 *
 * MIT License
 *
 * Copyright (c) 2020-2021 Florian "Sangar" Nücke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * -------------------------------------------------------------------------------
 *
 * All images / textures and localization strings (resources) are put in the
 * public domain, unless explicitly excluded below. More specifically, see CC0 1.0
 * Universal:
 *
 *       http://creativecommons.org/publicdomain/zero/1.0/
 *
 */

package me.ajh123.rebooted_computers.gui;

import me.ajh123.rebooted_computers.gui.widget.Texture;

public class Textures {
    public static final Texture COMPUTER_CONTAINER_TEXTURE = new Texture("textures/gui/widget/computer_container.png", 176, 197);
    public static final Texture ROBOT_CONTAINER_TEXTURE = new Texture("textures/gui/widget/robot_container.png", 176, 197);
    public static final Texture TERMINAL_SCREEN_TEXTURE = new Texture("textures/gui/widget/terminal_screen.png", 336, 208);
    public static final Texture MONITOR_SCREEN_TEXTURE = new Texture("textures/gui/widget/monitor_screen.png", 265, 208);
    public static final Texture BUS_INTERFACE_SCREEN_TEXTURE = new Texture("textures/gui/widget/bus_interface_screen.png", 240, 30);
    public static final Texture NETWORK_INTERFACE_CARD_SCREEN_TEXTURE = new Texture("textures/gui/widget/network_interface_card_screen.png", 176, 130);
    public static final Texture NETWORK_TUNNEL_SCREEN_TEXTURE = new Texture("textures/gui/widget/network_tunnel_screen.png", 176, 197);

    public static final Texture MONITOR_FOCUSED_TEXTURE = new Texture("textures/gui/overlay/monitor_focused.png", 265, 208);
    public static final Texture TERMINAL_FOCUSED_TEXTURE = new Texture("textures/gui/overlay/terminal_focused.png", 336, 208);
    public static final Texture SLOT_SELECTION_TEXTURE = new Texture("textures/gui/overlay/slot_selection.png", 18, 270);
    public static final Texture INFO_ICON_TEXTURE = new Texture("textures/gui/overlay/slot_info.png", 28, 28);
    public static final Texture WARN_ICON_TEXTURE = new Texture("textures/gui/overlay/slot_warn.png", 28, 28);
    public static final Texture BLOCK_FACE_FOCUSED_TEXTURE = new Texture("textures/gui/overlay/block_face_focused.png", 16, 16);
    public static final Texture BLOCK_FACE_ENABLED_TEXTURE = new Texture("textures/gui/overlay/block_face_enabled.png", 16, 16);
    public static final Texture BLOCK_FACE_DISABLED_TEXTURE = new Texture("textures/gui/overlay/block_face_disabled.png", 16, 16);

    public static final Texture HOTBAR_TEXTURE = new Texture("textures/gui/widget/hotbar.png", 224, 26);
    public static final Texture MONITOR_SIDEBAR_1_TEXTURE = new Texture("textures/gui/widget/monitor_sidebar_1.png", 19, 34);
    public static final Texture SIDEBAR_2_TEXTURE = new Texture("textures/gui/widget/sidebar_2.png", 19, 34);
    public static final Texture SIDEBAR_3_TEXTURE = new Texture("textures/gui/widget/sidebar_3.png", 19, 48);

    public static final Texture CONFIRM_BUTTON_TEXTURE = new Texture("textures/gui/widget/confirm_button.png", 27, 14);
    public static final Texture CANCEL_BUTTON_TEXTURE = new Texture("textures/gui/widget/cancel_button.png", 27, 14);
    public static final Texture ENERGY_TEXTURE = new Texture("textures/gui/widget/energy.png", 24, 26);
    public static final Texture POWER_BUTTON_TEXTURE = new Texture("textures/gui/widget/power_button.png", 42, 14);
    public static final Texture INPUT_BUTTON_TEXTURE = new Texture("textures/gui/widget/input_button.png", 42, 14);
    public static final Texture INVENTORY_BUTTON_TEXTURE = new Texture("textures/gui/widget/inventory_button.png", 28, 14);
    public static final Texture NETWORK_TUNNEL_LINK_BUTTON_TEXTURE = new Texture("textures/gui/widget/network_tunnel_link_button.png", 80, 40);
}
