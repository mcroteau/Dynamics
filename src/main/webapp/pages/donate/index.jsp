<html>
<head>
    <title>Dynamics +Gain: Make One-Time Donation</title>
</head>
<body>
    <style>
        h1{
            margin:0px 0px 20px ;
            line-height: 1.0em;
        }
        #make-donation-container{
            text-align: left;
        }
        #credit-card{
            padding:20px;
            background:#f0f4f5;
            border:solid 1px #eaeeef;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }
        input[type="text"],
        .InputElement{
            padding:20px;
            width:100%;
            font-size:27px !important;
        }
    </style>

    <h1>Make Contribution &hearts;</h1>

    <p class="open-text">Please select from the following:</p>

<%--    <h1 class="amount"></h1>--%>

    <div>
        <a href="javascript:" class="button light" data-amount="500" data-display="5.00">$5</a>&nbsp;
        <a href="javascript:" class="button yellow" data-amount="1000" data-display="10.00">$10</a>&nbsp;
        <a href="javascript:" class="button beauty" data-amount="3000" data-display="30.00">$30</a>
        <a href="javascript:" class="button purple" data-amount="5000" data-display="50.00">$50</a>&nbsp;
        <a href="javascript:" class="button modern" data-amount="10000" data-display="100.00">$100</a>&nbsp;
    </div>

    <input type="hidden" name="amount" id="amount-input" value=""/>

    <div id="make-donation-container">

        <label>Credit Card</label>

        <div id="credit-card"></div>
        <div id="processing" class="tiny" style="display:block;text-align:left;margin-top:10px;"></div>

        <form action="/z/donate" modelAttribute="oneDonation" method="post" id="donation-form">
            <input type="hidden" name="stripeToken" id="stripe-token" value=""/>

            <label>Email</label>
            <input type="text" name="email" placeholder="support@dynamicsgain.org"/>

            <p>We do not store credit card information! <br/>We count on Stripe to process all donations!</p>

            <div style="text-align: center;">
                <a href="javascript:" id="donate-button" class="button super amount" style="text-transform:none;">Make Donation</a>
            </div>

        </form>


    </div>

    <div style="text-align: left;margin-top:71px;">
        <a href="/z/donate" class="href-dotted">&larr; Back</a>
    </div>

    <script src="https://js.stripe.com/v3/"></script>
    <script>
        $(document).ready(function() {
            var $amount = $('.amount'),
                $amountInput = $('#amount-input'),
                $buttons = $('.button'),
                $donateButton = $('#donate-button')

            $buttons.click(function (evnt) {
                var $target = $(evnt.target)
                if(!$target.hasClass('super')) {

                    $buttons.removeClass('retro').removeClass('active')
                    $target.toggleClass('retro').addClass('active')
                    var amount = $target.attr('data-display')

                    console.log('donate', amount)
                    $amount.html('Make Donation $' + amount)
                    console.log($amount.html());
                    $amountInput.val($target.attr('data-amount'))
                }
            })

            $donateButton.click(function(){
                if(!$amountInput.val() == ''){

                }else{
                    alert('Please select an amount!')
                }
            })

            var stripe = {},
                elements = {},
                card = {};

            var $creditCard = $("#credit-card"),
                $processing = $("#processing"),
                $stripeToken = $("#stripe-token");

            stripe = Stripe("pk_test_KYVCbdaOAuezlE7sF7cn2hnK")
            elements = stripe.elements()

            card = elements.create('card', {
                base: {
                    fontSize: '29px',
                    lineHeight: '48px'
                }
            })
            card.mount('#credit-card')
            card.addEventListener('change', function (event) {
                if (event.error) {
                    $processing.html(event.error.message)
                    $processing.show()
                } else {
                    $processing.hide()
                    $processing.html("Processing...")
                }
            });

            $donateButton.click(function (event) {
                event.preventDefault()
                $processing.show()
                stripe.createToken(card).then(function (result) {
                    if (result.token.hasOwnProperty('id')) {
                        $stripeToken.value = result.token.id
                        $donationForm.submit()
                    } else {
                        $processing.html("Please notify us, nothing was charged... ")
                    }
                });
            })
        })
    </script>


</body>
</html>
