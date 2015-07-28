package me.Fupery.PlayerJobs.IO;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

public class NBTTree {

    HashMap<Tag, Object> tree;
    File file;

    public NBTTree (File file) {
        tree = new HashMap<>();
        this.file = file;
    }
    public NBTTree addCompoundTag(String tag) {
        CompoundTag compound = new CompoundTag(tag, 10);
        tree.put(compound, compound.tags);
        return this;
    }
    public NBTTree addStringTag(String tag, String value) {
        tree.put(new Tag(tag, 8), value);
        return this;
    }
    public NBTTree addList(String tag, List values) {
        tree.put(new Tag(tag, 9), values);
        return this;
    }
    public NBTTree addDouble(String tag, double value) {
        tree.put(new Tag(tag, 6), value);
        return this;
    }
    public byte[] writeTags() throws UnsupportedEncodingException {

        byte[] bytes = new CompoundTag("job", 10).getBytes();

        for (Tag tag : tree.keySet()) {
           addBytes(bytes, tag.getBytes());

        }


        return null;
    }
    public static byte[] addBytes(byte[] a1, byte[] a2) {
        int length = a1.length + a2.length;
        byte[] result = new byte[length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }
}
class Tag {

    String tag;
    byte type;

    Tag(String tag, int type) {
        this.tag = tag;
        this.type = ((byte) type);
    }
    public byte[] getBytes() throws UnsupportedEncodingException {
        byte[] tagName = tag.getBytes("UTF-8");
        int length = 3 + tagName.length;

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(((short) tag.length()));
        byte[] tagLength = buffer.array();

        byte[] result = new byte[length + 3];
        result[0] = type;
        System.arraycopy(tagLength, 0, result, 1, tagLength.length);
        System.arraycopy(tagName, 0, result, 3, result.length);

        return result;
    }
}
class CompoundTag extends Tag{

    HashMap<Tag, Object> tags;

    CompoundTag(String tag, int type) {
        super(tag, type);
        tags = new HashMap<>();
    }
}
