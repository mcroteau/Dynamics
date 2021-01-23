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
        <a href="javascript:" class="button yellow" data-amount="500" data-display="5.00">$5</a>&nbsp;
        <a href="javascript:" class="button light" data-amount="1000" data-display="10.00">$10</a>&nbsp;
        <a href="javascript:" class="button beauty" data-amount="3000" data-display="30.00">$30</a>
        <a href="javascript:" class="button purple" data-amount="5000" data-display="50.00">$50</a>&nbsp;
        <input type="text" name="custom" placeholder="Custom" style="width:110px;"/>
    </div>

    <input type="hidden" name="amount" id="amount-input" value=""/>

    <div id="make-donation-container" style="display:none;">

        <label>Credit Card</label>
        <input type="text" id="credit-card" value="4242424242424242"/>

        <label>month</label>
        <input type="text" id="exp-month" value="12"/>

        <label>year</label>
        <input type="text" id="exp-year" value="2021"/>

        <label>cvc</label>
        <input type="text" id="cvc" value="123"/>

        <label>Email</label>
        <input type="text" id="email" value="croteau.mike@gmail.com" placeholder="support@dynamicsgain.org"/>

        <div id="processing" class="tiny" style="display:block;text-align:left;margin-top:10px;"></div>

        <div style="text-align: center;">
            <a href="javascript:" id="donate-button" class="button super yellow amount" style="text-transform:none;">Donate</a>
        </div>



    </div>

    <div style="text-align: left;margin-top:71px;">
        <a href="/z/home" class="href-dotted">&larr; Back</a>
    </div>

<%--    <script src="https://js.stripe.com/v3/"></script>--%>
    <script>
        $(document).ready(function() {
            var $amount = $('.amount'),
                $amountInput = $('#amount-input'),
                $buttons = $('.button'),
                $donateButton = $('#donate-button'),
                $makeDonationContainer = $('#make-donation-container')


            $buttons.click(function (evnt) {
                var $target = $(evnt.target)
                if(!$target.hasClass('super')) {

                    // $buttons.removeClass('retro').removeClass('active')
                    // $target.toggleClass('retro')
                    $buttons.removeClass('active')
                    $target.toggleClass('active')
                    var amount = $target.attr('data-display')

                    console.log('donate', amount)
                    $amount.html('Donate $' + amount)
                    console.log($amount.html());
                    $amountInput.val($target.attr('data-display'))
                }

                $makeDonationContainer.fadeIn(300);
            })

            var $creditCard = $('#credit-card'),
                $expMonth = $('#exp-month'),
                $expYear = $('#exp-year'),
                $cvc = $('#cvc'),
                $email = $('#email'),
                $processing = $("#processing");


            $donateButton.click(function(){
                if(!$amountInput.val() == ''){
                    var raw = {
                        "amount" : $amountInput.val(),
                        "creditCard": $creditCard.val(),
                        "expMonth" : $expMonth.val(),
                        "expYear" : $expYear.val(),
                        "cvc" : $cvc.val(),
                        "email" : $email.val(),
                        "recurring" : true
                    };
                    console.log(data);

                    var data = JSON.stringify(raw)

                    $.ajax({
                        method: 'post',
                        url: '/z/donate/make',
                        data: data,
                        contentType: "application/json",
                        success: function(){
                            console.log('success')
                        },
                        error: function(e){
                            console.log('...', e)
                        }
                    })
                }else{
                    alert('Please select an amount!')
                }
            })

        })
    </script>


</body>
</html>
