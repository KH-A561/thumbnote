package net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils;

import java.util.HashMap;
import java.util.Map;

public enum CoverSrcUtils {
    ROYAL_HEATH(1, "https://i.imgur.com/yocqlk7.jpg"),
    EGG_SOUR(2, "https://i.imgur.com/GdsZdEJ.jpg"),
    SNOWY_MINT(3, "https://i.imgur.com/tZiGml7.jpg"),
    HOPBUSH(4, "https://i.imgur.com/nZJlkqE.jpg"),
    FLAX(5, "https://i.imgur.com/nMdpYd8.jpg"),
    WISTERIA(6, "https://i.imgur.com/B1vtOVR.jpg"),
    a(7, "https://i.imgur.com/gTLr6nG.jpg"),
    b(8, "https://i.imgur.com/0qQCD54.jpg"),
    c(9, "https://i.imgur.com/asnKW6G.jpg"),
    d(10, "https://i.imgur.com/OqKjaIc.jpg"),
    e(11, "https://i.imgur.com/HG6iraY.jpg"),
    f(12, "https://i.imgur.com/BVFCZeA.jpg"),
    g(13, "https://i.imgur.com/RsAOxWz.jpg"),
    h(14, "https://i.imgur.com/vxadELp.jpg"),
    i(15, "https://i.imgur.com/OHaEmvY.jpg"),
    j(16, "https://i.imgur.com/ldnpi1T.jpg"),
    k(17, "https://i.imgur.com/h3ByR6U.jpg"),
    l(18, "https://i.imgur.com/4WAKDhq.jpg"),
    m(19, "https://i.imgur.com/mdJqcs0.jpg"),
    n(20, "https://i.imgur.com/0XK8NlP.jpg");

    private static final Map<Integer, CoverSrcUtils> BY_ID = new HashMap<>();

    static {
        for (CoverSrcUtils e : values()) {
            BY_ID.put(e.getId(), e);
        }
    }

    public static String generateCover() {
        String src = BY_ID.get(idOfPicked).getSrc();
        idOfPicked++;
        if (idOfPicked > size) {
            idOfPicked = 1;
        }
        return src;
    }

    public int getId() {
        return id;
    }

    public CoverSrcUtils getById(int id) {
        return BY_ID.get(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    CoverSrcUtils(int id, String src) {
        this.id = id;
        this.src = src;
        this.isPicked = false;
    }

    CoverSrcUtils(int id, String src, boolean isPicked) {
        this.id = id;
        this.src = src;
        this.isPicked = isPicked;
    }

    private int id;
    private String src;
    private boolean isPicked;
    private static int size = BY_ID.size();
    private static int idOfPicked = 1;
}
