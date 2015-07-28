package me.Fupery.PlayerJobs.IO;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/**
 *  Simple class to write NBT data. <p>
 *  Add tags to build, then use writeTags()
 *  to get the completed byte array. <p>
 *  More tag types may be added if needed.
 */

public class NBTTree {

    HashMap<Tag, Object> tree;

    public NBTTree() {
        tree = new HashMap<>();
    }

    public NBTTree addCompoundTag(String tag) {
        NBTTree subTree = new NBTTree();
        CompoundTag compound = new CompoundTag(tag, this);
        tree.put(compound, compound);
        return subTree;
    }

    public NBTTree addByteTag(String tag, byte value) {
        tree.put(new Tag(tag, 1), value);
        return this;
    }

    public NBTTree addDoubleTag(String tag, double value) {
        tree.put(new Tag(tag, 6), value);
        return this;
    }

    public NBTTree addStringTag(String tag, String value) {
        tree.put(new Tag(tag, 8), value);
        return this;
    }

    public NBTTree addListTag(String tag, List values) {
        tree.put(new Tag(tag, 9), values);
        return this;
    }
// TODO - TAG READER
    public byte[] writeTags() throws UnsupportedEncodingException {
        return new CompoundTag("job", this).setTags(tree).getSubTags();
    }
    public HashMap<String, Object> readTags(byte[] bytes) {
        HashMap<String, Object> tags = new HashMap<>();

        Tag tag;

        for(byte b : bytes) {
        }

        return tags;
    }
}

class Tag {

    String tag;
    byte type;

    Tag(String tag, int type) {
        this.tag = tag;
        this.type = ((byte) type);
    }

    byte[] getBytes() throws UnsupportedEncodingException {
        //get length of tag name as 2-byte short
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(((short) tag.length()));
        byte[] tagLength = buffer.array();

        //get tag name as UTF-8 byte array
        byte[] tagName = tag.getBytes("UTF-8");
        int length = 3 + tagName.length;

        byte[] result = new byte[length];

        //tag type id
        result[0] = type;

        System.arraycopy(tagLength, 0, result, 1, tagLength.length);
        System.arraycopy(tagName, 0, result, 3, tagName.length);

        return result;
    }
}

class CompoundTag extends Tag {

    HashMap<Tag, Object> tags;
    NBTTree parentTree;

    CompoundTag(String tag, NBTTree tree) {
        super(tag, 10);
        tags = new HashMap<>();
        parentTree = tree;
    }
    NBTTree getParentTree() {
        return parentTree;
    }

    static byte[] getObjectBytes(byte type, Object o) throws UnsupportedEncodingException {
        ByteBuffer buffer;

        switch (type) {
            case 1:
                return new byte[]{((byte) o)};
            case 2:
                buffer = ByteBuffer.allocate(2);
                buffer.putShort(((short) o));
                return buffer.array();
            case 6:
                byte[] output = new byte[8];
                Long lng = Double.doubleToLongBits(((double) o));
                for (int i = 0; i < 8; i++) {
                    output[i] = (byte) ((lng >> ((7 - i) * 8)) & 0xff);
                }
                return output;
            case 8:
                String string = (String) o;
                byte[] tagName = string.getBytes("UTF-8");
                int length = 2 + tagName.length;

                buffer = ByteBuffer.allocate(2);
                buffer.putShort((short) string.length());
                byte[] tagLength = buffer.array();

                byte[] result = new byte[length];
                System.arraycopy(tagLength, 0, result, 0, tagLength.length);
                System.arraycopy(tagName, 0, result, tagLength.length, tagName.length);
                return result;
            case 10:
                return ((CompoundTag) o).getSubTags();

            default:
                return null;
        }
    }
    static byte[] addBytes(byte[] a1, byte[] a2, byte[] a3) {
        int length = a1.length + a2.length + a3.length;
        byte[] result = new byte[length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        System.arraycopy(a3, 0, result, a1.length + a2.length, a3.length);
        return result;
    }

    CompoundTag setTags(HashMap<Tag, Object> tags) {
        this.tags = tags;
        return this;
    }

    byte[] getSubTags() throws UnsupportedEncodingException {
        byte[] bytes = getBytes();

        for (Tag tag : tags.keySet()) {
            bytes = addBytes(bytes, tag.getBytes(), getObjectBytes(tag.type, tags.get(tag)));
        }
        byte[] result = new byte[bytes.length + 1];
        result[bytes.length] = 0;
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        return result;
    }
}