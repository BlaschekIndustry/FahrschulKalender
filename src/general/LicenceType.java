package general;

public enum LicenceType {
    LICENCE_TYPE_A, LICENCE_TYPE_B, LICENCE_TYPE_C, LICENCE_TYPE_D, LICENCE_TYPE_E ;

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

    public static LicenceType TypeOfXMLName(String type){
        switch (type){
            case "A": return LICENCE_TYPE_A;
            case "B": return LICENCE_TYPE_B;
            case "C": return LICENCE_TYPE_C;
            case "D": return LICENCE_TYPE_D;
            case "E": return LICENCE_TYPE_E;
        }
        return null;
    }
}
