/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2012 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion Client.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.net.client;

import illarion.client.net.CommandList;
import illarion.common.annotation.NonNull;
import illarion.common.net.NetCommWriter;
import illarion.common.types.ItemCount;
import illarion.common.types.Location;
import net.jcip.annotations.Immutable;

/**
 * Client Command: Dragging a item from a inventory slot to the game map ({@link CommandList#CMD_DRAG_INV_MAP}).
 *
 * @author Nop
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@Immutable
public final class DragInvMapCmd extends AbstractDragCommand {
    /**
     * The location on the map that is the target of the move operation.
     */
    @NonNull
    private final Location dstLoc;

    /**
     * Inventory position the drag starts at.
     */
    private final short srcPos;

    /**
     * Default constructor for the dragging from inventory to map command.
     */
    public DragInvMapCmd(final int source, @NonNull final Location destination, @NonNull final ItemCount count) {
        super(CommandList.CMD_DRAG_INV_MAP, count);
        srcPos = (short) source;
        dstLoc = new Location(destination);
    }

    @Override
    public void encode(@NonNull final NetCommWriter writer) {
        writer.writeUByte(srcPos);
        writer.writeLocation(dstLoc);
        getCount().encode(writer);
    }

    @NonNull
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return toString("Source: " + srcPos + " Destination: " + dstLoc + ' ' + getCount());
    }
}
