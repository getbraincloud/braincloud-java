package com.bitheads.braincloud.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ACL {
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

    public ACL() {
        _other = Access.None;
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

    public static ACL readOnlyOther() {
        ACL acl = new ACL();
        acl._other = Access.ReadOnly;
        return acl;
    }

    public static ACL readWriteOther() {
        ACL acl = new ACL();
        acl._other = Access.ReadWrite;
        return acl;
    }

    public static ACL createFromJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            return createFromJson(jsonObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return new ACL();
    }

    public static ACL createFromJson(JSONObject json) {
        ACL acl = new ACL();
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
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public String toJsonString() {
        try {
            JSONObject acl = new JSONObject();
            acl.put("other", _other.ordinal());
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

