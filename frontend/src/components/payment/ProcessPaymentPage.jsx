import { useSearchParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useError } from '../common/ErrorDisplay';
import Payment from "./Payment";


const ProcessPaymentPage = () => {

    const [searchParams] = useSearchParams();
    const [paymentComplete, setPaymentComplete] = useState(false);
    const [orderDetails, setOrderDetails] = useState({
        orderId: '',
        amount: 0
    });


    const navigate = useNavigate();
    const { ErrorDisplay, showError } = useError();

    useEffect(() => {
        const orderId = searchParams.get('orderId');
        const amount = searchParams.get('amount');
        
        if (!orderId || !amount) {
            showError("Missing order information in URL");
            return;
        }

        if(isNaN(amount) || Number(amount) <= 0) {
            showError("Invalid amount specified");
            return;
        }
        
        setOrderDetails({
            orderId,orderId,
            amount: Number(amount)
        });
    }, [searchParams]);

    const handlePaymentSuccess = (paymentIntent) => {
            console.log("Payment successful: ", paymentIntent);
            setPaymentComplete(true);
            
            
            setTimeout(() => {
                navigate('/my-order-history');
            }, 8000);            
        }

        if(paymentComplete) {
            return(
                <div className="payment-success">
                    <h2>Payment Successful!</h2>
                    <p>Thank you for your purchase. Order ID: {orderDetails.orderId}</p>
                    <p>You will receive an email of your payment success</p>
                </div>
            )
        }

    return (
        <div className="checkout-container">
            <ErrorDisplay />

            <Payment
                amount={orderDetails.amount}
                orderId={orderDetails.orderId}
                onSuccess={handlePaymentSuccess}
                
            />

        </div>
    )
}
export default ProcessPaymentPage;