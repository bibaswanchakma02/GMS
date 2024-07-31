package project2.gms.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String RAZORPAY_KEY_ID;

    @Value("${razorpay.key.secret}")
    private String RAZORPAY_KEY_SECRET;

    private RazorpayClient getRazorpayClient() throws RazorpayException {
        return new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
    }

//    public void capturePayment(String paymentId) throws RazorpayException {
//        RazorpayClient razorpayClient = getRazorpayClient();
//
//        Payment payment = razorpayClient.payments.fetch(paymentId);
//        if ("authorized".equals(payment.get("status"))) {
//            // Capture the payment
//            Map<String, Object> captureRequest = new HashMap<>();
//            captureRequest.put("amount", payment.get("amount"));
//            razorpayClient.payments.capture(paymentId, captureRequest);
//        }
//    }


    public Order createOrder(double amount) throws RazorpayException{
        RazorpayClient razorpayClient = getRazorpayClient();

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt");
        orderRequest.put("payment_capture", 1);

        return razorpayClient.orders.create(orderRequest);
    }

    public boolean verifyPayment(String paymentId) throws RazorpayException{
//        RazorpayClient razorpayClient = getRazorpayClient();
//        Payment payment = razorpayClient.payments.fetch(paymentId);
//
//        System.out.println("Payment Status: " + payment.get("status"));
//
//        return payment.get("status").equals("captured");

        RazorpayClient razorpayClient = getRazorpayClient();
        Payment payment = razorpayClient.payments.fetch(paymentId);

        String status = payment.get("status");
        System.out.println("Payment Status: " + status);  // For debugging

        if ("authorized".equals(status)) {
            // Capture the payment
            Map<String, Object> captureRequestMap = new HashMap<>();
            captureRequestMap.put("amount", payment.get("amount"));

            // Convert Map to JSONObject
            JSONObject captureRequest = new JSONObject(captureRequestMap);

            // Capture the payment
            razorpayClient.payments.capture(paymentId, captureRequest);

            // Fetch the updated payment status
            payment = razorpayClient.payments.fetch(paymentId);
            status = payment.get("status");
        }

        return "captured".equals(status);
    }


}
