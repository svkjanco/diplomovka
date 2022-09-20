package com.example.test2;

class ContentItem {

    final String name;
    final String desc;
    boolean isSection = false;

    ContentItem(String n) {
        name = n;
        desc = "";
        isSection = true;
    }

    ContentItem(String n, String d) {
        name = n;
        desc = d;
    }
}