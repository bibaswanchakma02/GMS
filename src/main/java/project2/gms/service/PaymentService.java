package project2.gms.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        RazorpayClient razorpayClient = getRazorpayClient();
        Payment payment = razorpayClient.payments.fetch(paymentId);

        return payment.get("status").equals("captured");
    }


}
