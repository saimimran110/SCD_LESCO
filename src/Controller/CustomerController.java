package Controller;

import java.util.ArrayList;
import lesco.bill.system.a1.pkg22l.pkg7906.BillingInfo;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.loginForCustomer;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.addCustomer;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.getCustomerName;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.viewBills;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.updateUnitsConsumed;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.updateCNICExpiryDate;
import static lesco.bill.system.a1.pkg22l.pkg7906.Customer.predictBill;

public class CustomerController {

    public boolean CUSTOMER_LOGIN(String id, String cnic) {
        return loginForCustomer(id, cnic);
    }

    public int ADD_CUSTOMER(String cnic, String name, String address, String phoneNum, String customerType, String meterType, String connectionDate) {
        return addCustomer(cnic, name, address, phoneNum, customerType, meterType, connectionDate);
    }

    public ArrayList<BillingInfo> VIEW_BILL(String customerId) {
        ArrayList<BillingInfo> list = viewBills(customerId);
        return list;
    }

    public boolean UPDATE_UNIT(String ci, int ru, int pu) {
        return updateUnitsConsumed(ci, ru, pu);
    }

    public String GIVE_NAME_OF_CUSTOMER(String id) {

        return getCustomerName(id);
    }

    public boolean UPDATE_CNIC_EXPIRY_DATE(String cnic, String customerId, int day, int month, int year) {
        return updateCNICExpiryDate(cnic, customerId, day, month, year);
    }

    public ArrayList<String> PRIDICT_BILL(String id, int ru, int pu) {

        ArrayList<String> list = predictBill(id, ru, pu);

        return list;

    }

}
