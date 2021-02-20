<html>
<head>
    <title>Developers API</title>
</head>
<body>

    <h1>Developers API</h1>

    <p class="left">We have an API in that allows you to integrate
    Dynamics <strong>+Gain's</strong> donations processor so you can make
        donations to the organization or shelter you love on your website or
        through your android app or ios app.
        All you need is the Organization's Id for the system.</p>

    <h2 style="margin-top:50px;">Making a Donation Request</h2>

    <p>Perform a <strong>POST</strong> request to the following endpoint.</p>
    <p class="yellow inline">https://www.dynamicsgain.org/z/donation/make</p>
    <p class="information">sample data payload below</p>
    <pre style="font-family:roboto-slab !important;display:inline-block;text-align:left; padding:20px; background: #f1f5f5;">
    {
        "creditCard": 4242424242424242,
        "expMonth" : 12,
        "expYear" : 2072,
        "cvc" : 123,
        "amount" : 123,
        "email" : croteau.mike@gmail.com,
        "recurring" : true|false,
        "location" : 1
    }</pre>

    <style>
        ul li{
            list-style: none;
        }
    </style>
    <h4>Payload definitions:</h4>
    <ul style="text-align: left;margin-left:30px;">
        <li><strong>creditCard</strong>: The donors credit card number, no spaces, dashes or special characters.</li>
        <li><strong>expMonth</strong> : The donors credit card expiration month, two digits.</li>
        <li><strong>expYear</strong> : The donors credit card expiration year, 4 digits.</li>
        <li><strong>cvc</strong> : The donors credit card cvc number, 3 digits.</li>
        <li><strong>amount</strong> : The amount to be donated, may include decimal but not required.</li>
        <li><strong>email</strong> : The donors email address, an account will be created for them. Their username and usable password for our system will be sent in the response named 'cleanPassword'.</li>
        <li><strong>recurring</strong> : true|false if recurring monthly donation</li>
        <li><strong>location (optional)</strong> : The Id of the Charitable Organization or Homeless Shelter that you would like to donate to on the system.
            Location is used throughout our system to reference a Homeless Shelter or Charitable Organization.
        If you leave blank you can make a donation to Dynamics +Gain instead.</li>
    </ul>

    <p>Sample Response:</p>
    <pre style="margin:0px;font-family:roboto-slab !important;display:inline-block;text-align:left; padding:20px; background: #f1f5f5;">
{
        "id":3,
        "amount":5,
        "chargeId":"ch_1IMiSxFMDPZBpdm3BTkZtNMn",
        or
        "subscriptionId":"sub_IygsY8XLranuNy",
        "userId":1,
        "locationId":1,
        "processed":true,
        "status":"Successfully processed donation!",
        "user":{
            "id":1,
            "username":"croteau.mike@gmail.com",
            "password":"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
            "cleanPassword" : "6RevZZK"
            "dateCreated":20210219000000,
            "stripeUserId":"cus_Iyfoxc5boHkxTf",
        },
        "location":{
            "id":1,
            "userId":0,
            "townId":1,
            "name":"Rescue Mission",
            "needs":"Shoes, Socks, Jackets, Laptops, Prepaid Phones",
            "count":0,
            "locationUri":"rescuemission0",
            "description":"Helping at-risk and homeless families in Clark
                County achieve sustainable housing and independence through a
                compassionate, community-based response.",
            "accounts":[],
            "counts":[]
        }
}</pre>

    <h2 style="margin-top:50px;">What if they want to cancel a monthly donation?</h2>

    <p>Perform a <strong>POST</strong> request to the following endpoint.</p>
    <p class="yellow inline">https://www.dynamicsgain.org/z/donation/cancel/{{subscription_id}}</p>
    <p class="information">or if they made a donation to an Charitable Organization or Homeless shelter.</p>
    <p class="yellow inline">https://www.dynamicsgain.org/z/donation/cancel/{{location_id}}/{{subscription_id}}</p>

    <h4>Path Parameters</h4>

    <ul style="text-align: left;margin-left:30px;">
        <li><strong>location_id</strong> : The Id of the Charitable Organization or Homeless Shelter in our system.</li>
        <li><strong>subscription_id</strong> : The Id of the monthly donation named <strong>subscriptionId</strong> in the initial donation response.</li>
    </ul>

    <h2 style="margin-top:50px;">Is it secure?</h2>
    <p class="left">All Requests are made over Https, the credit card is not stored in our system,
    we rely on Stripe.com. They handle all the compliance stuff! They made our lives easier. They are awesome!</p>


</body>
</html>
