<html>
<head>
    <title>Dynamics +Gain: Make Donation</title>
</head>
<body>
    <style>
        h1{
            margin:0px 0px 10px ;
            line-height: 1.0em;
        }
        #modal{
            position: fixed;
            top:0px;
            bottom:0px;
            left:0px;
            right:0px;
            margin:0px;
            background:#fff;
            display:none;
        }
        .message{
            margin-top:52px;
        }
        .duration{
            box-shadow: none !important;
        }
        .option{
            display: inline-block;
            vertical-align: middle;
            box-shadow: none !important;
        }
        #make-donation-container{
            text-align: left;
        }
        #custom,
        #custom:hover{
            font-size:19px !important;
            color:#fff !important;
            background: #7D5ABF;
            border:solid 3px #7D5ABF;
            padding: 12px 12px 11px 12px !important;
            display: inline-block;
            font-family: roboto-light !important;
            box-shadow: none !important;
            text-align: center;
            text-transform: none;
        }
        #custom,
        #custom:hover{
            color: #c4d3dd !important;
            background: #F3F3F7;
            border:solid 3px #F3F3F7;
        }
        #custom::placeholder{
            color: #8c96a9 !important;
        }
        #custom.active{
            color:#000 !important;
            background: #fff;
            border:solid 3px #3fb8ff;
        }
        input[type="text"],
        input[type="text"]:hover,
        input[type="text"]:focus,
        input[type="number"],
        input[type="number"]:hover,
        input[type="number"]:focus
        input[type="email"],
        input[type="email"]:hover,
        input[type="email"]:focus{
            width:100%;
            font-family: roboto-bold !important;
            padding: 13px 12px !important;
            line-height: 1.0em;
            font-size:27px !important;
        }
        input[type="number"]::placeholder,
        input[type="text"]::placeholder{
            color: #8c96a9;
            font-family: roboto !important;
        }
        .button.active{
            color:#fff;
            background: #3fb8ff;
            background: #fdfe01;
            border:solid 3px #8fd6ff;
            border:solid 3px #eeef07;
            background: #2234A3;
            border:solid 3px #fdfe01;
            background: #f540a6;
            font-family: roboto-bold !important;
        }
        .light.active:hover{
            background:#3fb8ff;
        }
        .light:hover{
            background: #efefef;
        }
        .cc-details{
            float:left;
            margin-right:20px;
        }
        .cc-details input[type="text"]{
            text-align: center;
        }
        #exp-month{
            width:90px;
        }
        #exp-year{
            width:120px;
        }
        #cvc{
            width:110px;
        }
    </style>

    <h1 class="live">Make Donation<strong class="">+</strong></h1>

    <p class="open-text live">Please select from the following:</p>

    <div id="donation-options" class="live">
        <div id="donation-durations">
            <button class="button retro small active duration">Give Once</button>
            <button class="button small sky duration" data-recurring="true">Give Monthly</button>
        </div>

        <a href="javascript:" class="option button sky active" id="fiver" data-amount="5">$5</a>&nbsp;
        <a href="javascript:" class="option button sky" data-amount="10">$10</a>&nbsp;
        <a href="javascript:" class="option button sky" data-amount="20">$20</a>&nbsp;
        <a href="javascript:" class="option button sky" data-amount="40">$40</a>&nbsp;
        <br/><br/>
        <input type="text" class="option button purple" id="custom" placeholder="Custom" style="width:110px;" data-amount="0"/>
    </div>

    <input type="hidden" name="amount" id="amount-input" value=""/>

    <div id="make-donation-container" class="live" style="display:none;">

        <label>credit card information</label>
        <input type="number" id="credit-card" placeholder="4242424242424242" maxlength="16"/>

        <div class="cc-details">
            <label>month</label>
            <input type="number" id="exp-month" placeholder="09" maxlength="2"/>
        </div>

        <div class="cc-details">
            <label>year</label>
            <input type="number" id="exp-year" placeholder="2027" maxlength="4"/>
        </div>

        <div class="cc-details">
            <label>cvc</label>
            <input type="number" id="cvc" placeholder="123" maxlength="3"/>
        </div>

        <br class="clear"/>

        <label>email</label>
        <input type="text" id="email" placeholder="support@dynamicsgain.org"/>

        <div style="text-align: center;">
            <a href="javascript:" id="donate-button" class="button super beauty amount" style="box-shadow:none !important;text-transform:none;">Donate +</a>
            <p id="contribution-type" class="information">One Time Donation</p>
        </div>

    </div>


    <div id="modal">
        <div id="processing" class="message" style="display:block">
            <h3>Processing... please wait</h3>
            <p>Your donation is being processed. Thank you for your patience.</p>
        </div>

        <div id="success" class="message" style="display:none">
            <h1>Thank you!</h1>
            <h2><strong id="donation-amount"></strong> Donated</h2>
            <p>Successfully processed your donation.</p>

            <p>Your username : <strong id="username"></strong></p>
            <p>Your temporary password : <strong id="password"></strong></p>

            <p><a href="/z/user/reset" class="href-dotted">Reset password</a></p>
            <p><a href="/z/signin" class="href-dotted">Signin</a></p>

            <a href="/z/home" class="href-dotted">Take me home...</a>
        </div>

        <div id="error-container" class="message" style="display:none">
            <h3>There was an issue...</h3>
            <p id="error"></p>
            <a href="/z/donate">Back</a>
        </div>
    </div>

    <div style="text-align: left;margin-top:71px;">
        <a href="/z/home" class="href-dotted">&larr; Back</a>
    </div>

    <script>
        $(document).ready(function() {

            var processingDonation = false,
                recurring = false;

            var $amount = $('.amount'),
                $amountInput = $('#amount-input'),
                $options = $('.option'),
                $durations = $('.duration'),
                $donateButton = $('#donate-button'),
                $donationOptions = $('#donation-options'),
                $makeDonationContainer = $('#make-donation-container'),
                $custom = $('#custom'),
                $fiver = $('#fiver'),
                $success = $('#success'),
                $contributionType = $('#contribution-type'),
                $live = $('.live');

            var $creditCard = $('#credit-card'),
                $expMonth = $('#exp-month'),
                $expYear = $('#exp-year'),
                $cvc = $('#cvc'),
                $email = $('#email'),
                $modal = $('#modal'),
                $processing = $("#processing");

            var $username = $('#username'),
                $password = $('#password'),
                $donationAmount = $('#donation-amount');


            $custom.focus(function(){
                $custom.val('');
                $custom.addClass('active')
            })

            $custom.mouseleave(function(){
                if($custom.val() == ''){
                    $custom.attr('placeholder', 'Custom')
                }
            })

            $custom.change(function(){
                var value = $custom.val()
                if(!isNaN(value)){
                    $amount.html('Donate $' + value)
                    $amountInput.val(amount)
                }else{
                    alert('Please enter a valid amount');
                }
            })

            $durations.click(function(){
                $durations.removeClass('retro').addClass('sky')
                $durations.removeClass('active')
                $(this).addClass('active').removeClass('sky')
                if($(this).attr('data-recurring') == 'true'){
                    $contributionType.html('Monthly Donation')
                    recurring = true
                }else{
                    $contributionType.html('One-Time Donation')
                    recurring = false
                }
            })

            $options.click(function (evnt) {
                var $target = $(evnt.target)
                if(!$target.hasClass('super')) {

                    $options.removeClass('active')
                    $target.toggleClass('active')
                    var amount = $target.attr('data-amount')

                    if(amount != '' &&
                            amount != 0 &&
                                !isNaN(amount)) {
                        $amount.html('Donate $' + amount)
                        $amountInput.val(amount)
                    }
                }

                $makeDonationContainer.fadeIn(300);
            })


            $donateButton.click(function(){
                if(!processingDonation &&
                        isValidForm()){

                    $processing.show()
                    $modal.show()
                    // $live.hide()

                    processingDonation = true;

                    var raw = getRaw()
                    var data = JSON.stringify(raw)

                    $.ajax({
                        method: 'post',
                        url: '/z/donate/make',
                        data: data,
                        contentType: "application/json",
                        success: function(resp){
                            console.log('success', resp)
                            var data = JSON.parse(resp)
                            console.log(data);
                            $processing.hide()
                            if(data.processed){
                                if('cleanPassword' in data.user){
                                    $username.html(data.user.username)
                                    $password.html(data.user.cleanPassword)
                                }
                                if(!('cleanPassword' in data.user)){
                                    $username.parents('p').hide()
                                    $password.parents('p').hide()
                                }
                                $donationAmount.html('$' + data.amount)
                                $success.fadeIn(100)
                            }else{
                                $('#error').html(data.status)
                                $('#error-container').show()
                            }
                        },
                        error: function(e){
                            $processing.hide()
                            console.log('...', e)
                        }
                    })
                }
            })

            var getRaw = function(){
                return {
                    "amount" : $amountInput.val(),
                    "creditCard": $creditCard.val(),
                    "expMonth" : $expMonth.val(),
                    "expYear" : $expYear.val(),
                    "cvc" : $cvc.val(),
                    "email" : $email.val(),
                    "recurring" : recurring
                };
            }

            var isValidForm = function(){
                if($amountInput.val() == ''){
                    alert('Please select an option or enter a custom amount?')
                    return false
                }
                if($creditCard.val() == ''){
                    alert('Please enter a valid credit card #')
                    return false
                }
                if($expMonth.val() == ''){
                    alert('Please enter a valid expiration month')
                    return false
                }
                if($expYear.val() == ''){
                    alert('Please enter a valid expiration year')
                    return false
                }
                if($cvc.val() == ''){
                    alert('Please enter a valid cvc')
                    return false
                }
                if($email.val() == ''){
                    alert('Please enter a valid email address')
                    return false
                }
                return true
            }


            $fiver.click()
        })
    </script>


</body>
</html>
