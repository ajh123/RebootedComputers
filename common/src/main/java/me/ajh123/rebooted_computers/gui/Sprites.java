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

import me.ajh123.rebooted_computers.gui.widget.Sprite;

import static me.ajh123.rebooted_computers.gui.Textures.*;

public class Sprites {
    public static final Sprite COMPUTER_CONTAINER = new Sprite(COMPUTER_CONTAINER_TEXTURE);
    public static final Sprite ROBOT_CONTAINER = new Sprite(ROBOT_CONTAINER_TEXTURE);
    public static final Sprite TERMINAL_SCREEN = new Sprite(TERMINAL_SCREEN_TEXTURE);
    public static final Sprite MONITOR_SCREEN = new Sprite(MONITOR_SCREEN_TEXTURE);
    public static final Sprite BUS_INTERFACE_SCREEN = new Sprite(BUS_INTERFACE_SCREEN_TEXTURE);
    public static final Sprite NETWORK_INTERFACE_CARD_SCREEN = new Sprite(NETWORK_INTERFACE_CARD_SCREEN_TEXTURE);
    public static final Sprite NETWORK_TUNNEL_SCREEN = new Sprite(NETWORK_TUNNEL_SCREEN_TEXTURE);

    public static final Sprite MONITOR_FOCUSED = new Sprite(MONITOR_FOCUSED_TEXTURE);
    public static final Sprite TERMINAL_FOCUSED = new Sprite(TERMINAL_FOCUSED_TEXTURE);
    public static final Sprite SLOT_SELECTION = new Sprite(SLOT_SELECTION_TEXTURE, 18, 18, 0, 0);
    public static final Sprite INFO_ICON = new Sprite(INFO_ICON_TEXTURE);
    public static final Sprite WARN_ICON = new Sprite(WARN_ICON_TEXTURE);

    public static final Sprite HOTBAR = new Sprite(HOTBAR_TEXTURE);
    public static final Sprite MONITOR_SIDEBAR_1 = new Sprite(MONITOR_SIDEBAR_1_TEXTURE);
    public static final Sprite SIDEBAR_2 = new Sprite(SIDEBAR_2_TEXTURE);
    public static final Sprite SIDEBAR_3 = new Sprite(SIDEBAR_3_TEXTURE);

    public static final Sprite ENERGY_BASE = new Sprite(ENERGY_TEXTURE, 12, 26, 0, 0);
    public static final Sprite ENERGY_BAR = new Sprite(ENERGY_TEXTURE, 12, 26, 12, 0);

    public static final Sprite INPUT_BUTTON_ACTIVE = new Sprite(INPUT_BUTTON_TEXTURE, 12, 12, 1, 1);
    public static final Sprite INPUT_BUTTON_BASE = new Sprite(INPUT_BUTTON_TEXTURE, 12, 12, 15, 1);
    public static final Sprite INPUT_BUTTON_PRESSED = new Sprite(INPUT_BUTTON_TEXTURE, 12, 12, 29, 1);

    public static final Sprite POWER_BUTTON_ACTIVE = new Sprite(POWER_BUTTON_TEXTURE, 12, 12, 1, 1);
    public static final Sprite POWER_BUTTON_BASE = new Sprite(POWER_BUTTON_TEXTURE, 12, 12, 15, 1);
    public static final Sprite POWER_BUTTON_PRESSED = new Sprite(POWER_BUTTON_TEXTURE, 12, 12, 29, 1);

    public static final Sprite INVENTORY_BUTTON_INACTIVE = new Sprite(INVENTORY_BUTTON_TEXTURE, 12, 12, 1, 1);
    public static final Sprite INVENTORY_BUTTON_ACTIVE = new Sprite(INVENTORY_BUTTON_TEXTURE, 12, 12, 15, 1);

    public static final Sprite NETWORK_TUNNEL_LINK_BUTTON_INACTIVE = new Sprite(NETWORK_TUNNEL_LINK_BUTTON_TEXTURE, 80, 20, 0, 0);
    public static final Sprite NETWORK_TUNNEL_LINK_BUTTON_ACTIVE = new Sprite(NETWORK_TUNNEL_LINK_BUTTON_TEXTURE, 80, 20, 0, 20);

    public static final Sprite CONFIRM_PRESSED = new Sprite(CONFIRM_BUTTON_TEXTURE, 12, 12, 14, 1);
    public static final Sprite CONFIRM_BASE = new Sprite(CONFIRM_BUTTON_TEXTURE, 12, 12, 1, 1);
    public static final Sprite CANCEL_PRESSED = new Sprite(CANCEL_BUTTON_TEXTURE, 12, 12, 14, 1);
    public static final Sprite CANCEL_BASE = new Sprite(CANCEL_BUTTON_TEXTURE, 12, 12, 1, 1);
}
