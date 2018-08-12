package com.hua.cache.server.data.model;

import com.hua.cache.server.util.Index;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CatNode implements Serializable,Index{
    private int catId;
    private int parentId;
    private List<Integer> childList;
    private int[] child;
    private int recordId;
    private int level;

    public CatNode(Category cat){
        this.childList = new ArrayList();
        this.catId = cat.getCatId();
        this.parentId = cat.getParentId();
        this.level = cat.getLevel();
        this.recordId = -1;
    }
    public CatNode(){

    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List getChildList() {
        return childList;
    }

    public void setChildList(List childList) {
        this.childList = childList;
    }

    public void setChild(int[] child) {
        this.child = child;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        String tab = "";
        for (int i = 0; i < level; i++) {
            tab+="  ";
        }
        return tab+"CatNode{" +
                "catId=" + catId +
                ", parentId=" + parentId +
                ", childList=" + ((childList== null) ? Arrays.toString(child) : childList) +
                ", recordId=" + recordId +
                '}';
    }
    @Override
    public void write(ByteBuffer bb) {
        bb.putInt(catId);
        bb.putInt(parentId);
        bb.putInt(recordId);
        int size = childList.size();
        bb.putInt(size);
        for (int i = 0; i < size; i++) {
            int subCatId = childList.get(i);
            bb.putInt(subCatId);
        }
    }

    public CatNode read(ByteBuffer bb) {
        this.catId = bb.getInt();
        this.parentId = bb.getInt();
        this.recordId = bb.getInt();
        int size = bb.getInt();
        child = new int[size];
        for (int i = 0; i < size; i++) {
            child[i] = bb.getInt();
        }
        return this;
    }

    @Override
    public int getSize() {
        int subSize = (childList==null)?child.length:childList.size();
        int size = (4 + subSize) * 4;
        return size;
    }
}
