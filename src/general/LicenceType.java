package general;

public enum LicenceType {
    LICENCE_TYPE_A, LICENCE_TYPE_B, LICENCE_TYPE_C, LICENCE_TYPE_D, LICENCE_TYPE_E;

    public static String XMLNameOfType(LicenceType type){
        switch (type){
            case LICENCE_TYPE_A: return "A";
            case LICENCE_TYPE_B: return "B";
            case LICENCE_TYPE_C: return "C";
            case LICENCE_TYPE_D: return "D";
            case LICENCE_TYPE_E: return "E";
        }
        return "";
    }
}
