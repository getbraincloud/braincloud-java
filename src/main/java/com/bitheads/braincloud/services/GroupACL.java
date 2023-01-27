package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupACL {
    public enum Access {
        None,
        ReadOnly,
        ReadWrite;

        public static Access fromOrdinal(int val) {
            for (int i = 0, ilen = values().length; i < ilen; ++i) {
                if (i == val) {
                    return values()[i];
                }
            }
            return None;
        }
    }

    private Access _other;
    private Access _member;

    public GroupACL() {
        _other = Access.None;
        _member = Access.None;
    }

    public GroupACL(Access other, Access member) {
        _other = other;
        _member = member;
    }

    public Access getOther() {
        return _other;
    }

    public void setOther(Access value) {
        _other = value;
    }

    public void setOther(int value) {
        _other = Access.fromOrdinal(value);
    }

    public Access getMember() {
        return _member;
    }

    public void setMember(Access value) {
        _member = value;
    }

    public void setMember(int value) {
        _member = Access.fromOrdinal(value);
    }

    public static GroupACL createFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            return createFromJson(jsonObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return new GroupACL();
    }

    public static GroupACL createFromJson(JSONObject json) {
        GroupACL acl = new GroupACL();
        acl.readFromJson(json);
        return acl;
    }

    public void readFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            readFromJson(jsonObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public void readFromJson(JSONObject json) {
        try {
            setOther(json.getInt("other"));
            setMember(json.getInt("member"));
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public String toJsonString() {
        try {
            JSONObject acl = new JSONObject();
            acl.put("other", _other.ordinal());
            acl.put("member", _member.ordinal());
            return acl.toString();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}

