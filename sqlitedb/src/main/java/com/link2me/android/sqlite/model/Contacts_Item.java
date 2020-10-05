package com.link2me.android.sqlite.model;

public class Contacts_Item {
    String contactId;
    String contactName;
    String contactmobileNO;
    String contactofficeNO;
    String contactKey;

    public Contacts_Item() {
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactmobileNO() {
        return contactmobileNO;
    }

    public void setContactmobileNO(String contactmobileNO) {
        this.contactmobileNO = contactmobileNO;
    }

    public String getContactofficeNO() {
        return contactofficeNO;
    }

    public void setContactofficeNO(String contactofficeNO) {
        this.contactofficeNO = contactofficeNO;
    }

    public String getContactKey() {
        return contactKey;
    }

    public void setContactKey(String contactKey) {
        this.contactKey = contactKey;
    }
}
