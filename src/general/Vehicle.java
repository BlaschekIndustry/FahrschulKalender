package general;

import java.util.ArrayList;

public class Vehicle {
    public static final String XML_VEHICLES_GROUP_IDENT = "vehicles";
    public static final String XML_VEHICLES_IDENT = "vehicle";
    public static final String XML_VEHICLES_NAME_IDENT = "vName";
    public static final String XML_VEHICLES_IS_EXAM_PERMITT = "vIsExamPermit";
    public static final String XML_VEHICLES_IS_TRAILER = "vIsTrailer";
    public static final String XML_VEHICLES_IS_AUTOMATIC = "vIsAutomatic";
    public static final String XML_VEHICLES_LICENCETYPE_IDENT = "vLicence";

    private String name;
    private Boolean isExamPermit;
    private Boolean isTrailer;
    private Boolean isAutomatic;
    private ArrayList<LicenceType> licenceTypes;

    public Vehicle(){
        this("Neues Fahrzeug", false, false, false, null);
        licenceTypes = new ArrayList<>();
        licenceTypes.add(LicenceType.LICENCE_TYPE_B);
    }

    public Vehicle(Vehicle vehicle){
        this(vehicle.name, vehicle.isExamPermit, vehicle.isTrailer, vehicle.isAutomatic, vehicle.licenceTypes);
    }

    public Vehicle(String name, Boolean isExamPermit, Boolean isTrailer, Boolean isAutomatic, ArrayList<LicenceType> licenceTypes) {
        this.name = name;
        this.isExamPermit = isExamPermit;
        this.isTrailer = isTrailer;
        this.isAutomatic = isAutomatic;
        if(licenceTypes != null)
            this.licenceTypes = licenceTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamPermit(Boolean examPermit) {
        isExamPermit = examPermit;
    }

    public void setTrailer(Boolean trailer) {
        isTrailer = trailer;
    }

    public void setAutomatic(Boolean automatic) { isAutomatic = automatic; }

    public void setLicenceTypes(ArrayList<LicenceType> licenceTypes) {
        this.licenceTypes = licenceTypes;
    }

    public String getName() {
        return name;
    }

    public Boolean isExamPermit() {
        return isExamPermit;
    }

    public Boolean isTrailer() {
        return isTrailer;
    }

    public Boolean isAutomatic() { return isAutomatic;   }

    public ArrayList<LicenceType> getLicenceTypes() {
        return licenceTypes;
    }
}
