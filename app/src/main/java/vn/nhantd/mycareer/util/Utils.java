package vn.nhantd.mycareer.util;

import org.bson.types.ObjectId;

public class Utils {
    public static String convertUidTo_id(String uid) {
        String _id = new ObjectId(HexUtils.convertStringToHex(uid)).toHexString();

        return _id;
    }
}
