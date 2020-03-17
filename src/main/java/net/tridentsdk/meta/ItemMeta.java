/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2017 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tridentsdk.meta;

import net.tridentsdk.meta.nbt.Tag;

import javax.annotation.concurrent.Immutable;
import java.io.DataOutputStream;

/**
 * A class representing an item's extra metadata such as
 * enchantments, attributes, item-specific data such as
 * potion and skull metas, etc...
 *
 * @author TridentSDK
 * @since 0.5-alpha
 */
@Immutable
public class ItemMeta {
    /**
     * The NBT data which contains modifications made to
     * this item.
     */
    private final Tag.Compound nbt;

    /**
     * Creates a new empty meta object for items
     */
    public ItemMeta() {
        this.nbt = new Tag.Compound("tag");
    }

    /**
     * Creates a new item meta object with the attributes
     * specified in the "tag" tag of the given compound
     *
     * @param compound the tag compound to read data
     */
    public ItemMeta(Tag.Compound compound) {
        this.nbt = compound;
    }

    /**
     * Writes out the NBT value for this item metadata to
     * the given stream.
     *
     * @param stream the stream to write
     */
    public void writeNbt(DataOutputStream stream) {
        this.nbt.write(stream);
    }
}