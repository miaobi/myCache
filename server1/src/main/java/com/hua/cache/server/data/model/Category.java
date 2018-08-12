package com.hua.cache.server.data.model;

public class Category {
    private Integer id;

    private String catName;

    private Integer catId;

    private Integer level;

    private Integer parentId;

    private Integer siteId;

    private Integer topId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName == null ? null : catName.trim();
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getTopId() {
        return topId;
    }

    public void setTopId(Integer topId) {
        this.topId = topId;
    }

    @Override
    public String toString() {
        String tab = "";
        if(level!=null){
            for (int i = 0; i < level; i++) {
                tab+="  ";
            }
        }
        return tab+"Category{" +
                "id=" + id +
                ", catName='" + catName + '\'' +
                ", catId=" + catId +
                ", level=" + level +
                ", parentId=" + parentId +
                ", siteId=" + siteId + ",topId="+topId+
                '}';
    }
}