package xyz.newtouch.crowdfunding.pojo;

import java.util.ArrayList;
import java.util.List;

public class TMenu {
    private Integer id;

    private Integer pid;

    private String name;

    private String icon;

    private String url;

    /**
     * 用于存放子菜单
     */
    private List<TMenu> submenus = new ArrayList<>();

    public List<TMenu> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<TMenu> submenus) {
        this.submenus = submenus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    @Override
    public String toString() {
        return "TMenu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", submenus=" + submenus +
                '}';
    }
}