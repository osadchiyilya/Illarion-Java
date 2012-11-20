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
package illarion.client.net.server.events;

/**
 * This event is send once the server sends a completed event for a crafting dialog
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class DialogCraftingUpdateCompletedReceivedEvent {
    /**
     * The ID of the dialog.
     */
    private final int requestId;

    /**
     * The amount of items that remain to be produced automatically.
     */
    private final int remaining;

    /**
     * Create a new instance of this event.
     *
     * @param requestId the ID of the dialog that is updated
     * @param remaining the amount of items remaining to be produced
     * @throws IllegalArgumentException in case the {@code remaining} is less then {@code 0}
     */
    public DialogCraftingUpdateCompletedReceivedEvent(final int requestId, final int remaining) {
        if (remaining < 0) {
            throw new IllegalArgumentException("The remaining item count must not be less then zero");
        }

        this.requestId = requestId;
        this.remaining = remaining;
    }

    /**
     * Get the ID of this dialog.
     *
     * @return the ID of the dialog
     */
    public int getDialogId() {
        return requestId;
    }

    /**
     * Get the amount of items remaining to be produced.
     *
     * @return the amount of items remaining
     */
    public int getRemaining() {
        return remaining;
    }
}
