package com.example.shailendra.admin.database;

public class MataData {

    // -----------  MetaData ---------------

    /*
     ---------- Admin Attributes...---------
     Check : is admin email is exist or not...
     -----> Path : PERMISSION / ADMIN_EMAILS / Data
     admin_email : "Email permited.."
     permission : "A"
     admin Type :
     1. A
     2. B
     3. C

     ------------------------------
     path :  ADMINS / ADMIN_MAIN / Data...
                    Doc Name - Mobile

     permission : "YES"

     admin_email : "Admin Email "
     admin_mobile :
     admin_name : "
     admin_photo : "Profile Photo Link "
     admin_address : "Full address..."
     admin_add_pin : 462021

     admin_type : "A" // Class of Admin..

     admin_id : "ADMIN_MAIN"
     admin_auth_id :


     */

    /// Admin Collection Reference
    /*
    CollectionReference collectionReference = firebaseFirestore.collection( "ADMIN_PER" ).document( cityName.toUpperCase() )
                .collection( "PERMISSION" );

     */

    /**  -------- User Order... Customer Order Process,
     * Step 1 : create a random order ID
     * step 2 : check that Oder ID that is exist on database or not..
     * step 3 : if Oder ID already exist then go Back (step 1) to New Order ID...
     * step 4 : if Order ID is new then create a new Document with this order ID
     * step 5 : if Task is failed then go back (step 1)...
     * step 6 : if Task is Success Go Process to payment...
     * step 7 : if PayMode COD go to (step 12) :
     * step 8 : if PayMode Online then check status - ( a - Success, b - Pending, c - Failed )
     * step 9 : if PayStatus - Failed : stored Info in order Document and exit.
     * step 10 : if PayStatus - Pending : stored Info in order Document and wait for get response...
     * step 11 : if PayStatus - Success : go to ( Step 12)
     * step 12 : Update order document and create collection to track order...
     * step 13 : check delivery_status and tracking details until order is not placed...
     * step 14 :
     *          delivery Status :
     *          (1) WAITING : when order is not accepted by admin...
     *          (2) ACCEPTED : when order has been accepted...
     *          (3) PROCESS : When order is in process to delivery...
     *          (4) CANCELLED : When Order has been cancelled by user...
     *          (5) SUCCESS : when order has been delivered successfully...
     *          (6) FAILED : when PayMode Online and payment has been failed...
     *          (7) PENDING : when Payment is Pending
     *
     * step 15 : if delivery status : CANCELLED then Query to get payment Return...
     * step 16 : Update Order history..!!
     *
     */



}
