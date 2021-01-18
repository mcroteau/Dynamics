/** Roboto Slab **/

@font-face {
    font-family: roboto-slab-thin;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-Thin.ttf');
}
@font-face {
    font-family: roboto-slab-extralight;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-ExtraLight.ttf');
}
@font-face {
    font-family: roboto-slab-light;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-Light.ttf');
}

@font-face {
    font-family: roboto-slab;
    src: url("/z/assets/css/fonts/roboto-slab/RobotoSlab-Regular.ttf");
}
@font-face {
    font-family: roboto-slab-medium;
    src: url("/z/assets/css/fonts/roboto-slab/RobotoSlab-Medium.ttf");
}
@font-face {
    font-family: roboto-slab-semibold;
    src: url("/z/assets/css/fonts/roboto-slab/RobotoSlab-SemiBold.ttf");
}
@font-face {
    font-family: roboto-slab-bold;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-Bold.ttf');
}
@font-face {
    font-family: roboto-slab-extrabold;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-ExtraBold.ttf');
}
@font-face {
    font-family: roboto-slab-black;
    src: url('/z/assets/css/fonts/roboto-slab/RobotoSlab-Black.ttf');
}

/**  Roboto **/
@font-face {
    font-family: roboto-thin;
    src: url('/z/assets/css/fonts/roboto/Roboto-Thin.ttf');
}
@font-face {
    font-family: roboto-light;
    src: url('/z/assets/css/fonts/roboto/Roboto-Light.ttf');
}
@font-face {
    font-family: roboto;
    src: url("/z/assets/css/fonts/roboto/Roboto-Regular.ttf");
}
@font-face {
    font-family: roboto-medium;
    src: url("/z/assets/css/fonts/roboto/Roboto-Medium.ttf");
}
@font-face {
    font-family: roboto-bold;
    src: url('/z/assets/css/fonts/roboto/Roboto-Bold.ttf');
}
@font-face {
    font-family: roboto-black;
    src: url('/z/assets/css/fonts/roboto/Roboto-Black.ttf');
}


/* http://meyerweb.com/eric/tools/css/reset/
   v2.0 | 20110126
   License: none (public domain)
*/

html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed,
figure, figcaption, footer, header, hgroup,
menu, nav, output, ruby, section, summary,
time, mark, audio, video {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
	font-family: roboto !important;
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure,
footer, header, hgroup, menu, nav, section {
	display: block;
}
body {
	line-height: 1;
}
ol, ul {
	list-style: none;
}
blockquote, q {
	quotes: none;
}
blockquote:before, blockquote:after,
q:before, q:after {
	content: '';
	content: none;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
}



html, body,
div, span, p, blockquote,
h1, h2, h3, h4,
input, select, textarea,
input::placeholder,
textarea::placeholder, th, td{
    vertical-align: middle;
	font-family: roboto !important;
	color:#000;
}
td{
    padding:10px 0px 10px 0px;
}

p, span{
    font-family: roboto-slab !important;
    line-height: 1.3em;
}

/*csslint box-model:false*/
/*
Box-model set to false because we're setting a height on select elements, which
also have border and padding. This is done because some browsers don't render
the padding. We explicitly set the box-model for select elements to border-box,
so we can ignore the csslint warning.
*/

h1{
    color:#000;
	font-size:49px;
	margin:11px 0px 0px;
	line-height:1.3em;
	font-family:roboto-bold !important;
}

h2{
    font-size:32px;
    font-family:roboto-bold !important;
}

h3{
    color:#000;
	font-size:26px;
	font-family:roboto-bold !important;
	line-height:1.17;
    margin:10px 0px;
}

p{
    margin:10px 0px;
}

textarea:focus,
textarea.typing {
    outline:none;
}

textarea::-webkit-input-placeholder { /* Chrome/Opera/Safari */
    font-family:Roboto-thin !important;
    color:#5f6f70;
    font-size:21px;
    text-shadow: 0px 0px #fff;
}
textarea::-moz-placeholder { /* Firefox 19+ */
    font-family:Roboto-thin !important;
    color:#5f6f70;
    text-shadow: 0px 0px #fff;
}
textarea:-ms-input-placeholder { /* IE 10+ */
    font-family:Roboto-thin !important;
    color:#5f6f70;
    text-shadow: 0px 0px #fff;
}
textarea:-moz-placeholder { /* Firefox 18- */
    font-family:Roboto-thin !important;
    color:#5f6f70;
    text-shadow: 0px 0px #fff;
}


input[type="text"]::-webkit-input-placeholder,
input[type="email"]::-webkit-input-placeholder,
input[type="password"]::-webkit-input-placeholder { /* Edge */
    color: #5f6f70;
    font-family:Roboto-light !important;
}

input[type="text"]:-ms-input-placeholder { /* Internet Explorer 10-11 */
    color: #5f6f70;
    font-family:Roboto-light !important;
}

input[type="text"]::placeholder,
input[type="email"]::placeholder,
input[type="password"]::placeholder {
    color: #5f6f70;
    font-family:Roboto-light !important;
}

input[type="file"]{
    cursor:pointer;
    cursor:hand;
    z-index:1;
}


.button,
input[type="button"].button,
input[type="reset"].button,
input[type="submit"].button{
	cursor:pointer;
	text-transform:uppercase;
	color:#fff;
	font-size:13px;
	padding:14px 18px;
    line-height: 1.0em;
	display:inline-block;
	background:#407C7C;
    border:solid 1px #407C7C;
	text-decoration:none;
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
    font-family:Roboto !important;
	outline:none !important;
	-webkit-appearance: none;
	-moz-appearance:none;
    -webkit-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    -moz-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
}

.button:active{
    -webkit-transition: 3s;
    -moz-transition: 3s;
    -ms-transition: 3s;
    -o-transition: 3s;
    transition: 3s;
    -webkit-box-shadow: 0px 1px 7px 0px rgba(179,179,179,0.43);
    -moz-box-shadow: 0px 1px 7px 0px rgba(179,179,179,0.43);
    box-shadow: 0px 1px 7px 0px rgba(179,179,179,0.43);
}

.button:hover{
    color:#fff;
	-webkit-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    -moz-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
}


.button.small,
input[type="button"].button.small,
input[type="reset"].button.small,
input[type="submit"].button.small{
	font-size:11px;
	padding:15px 23px;
}

.button.remove,
input[type="button"].remove,
input[type="reset"].remove,
input[type="submit"].remove{
	background:#CE6A6A;
	border:solid 1px #CE6A6A;
}

.yellow,
.button.yellow,
input[type="button"].button.yellow,
input[type="reset"].button.yellow,
input[type="submit"].button.yellow{
	color:#000;
	background:#f8f90c;
	border:solid 1px #f8f90c;
}


.button.sky,
input[type="button"].button.sky,
input[type="reset"].button.sky,
input[type="submit"].button.sky,
input[type="button"].sky{
	color:#000;
	background:#F3F3F7;
	border:solid 1px #F3F3F7;
}


.button.light,
input[type="button"].button.light,
input[type="reset"].button.light,
input[type="submit"].button.light,
input[type="button"].button.light{
	color:#000;
	background:#fff;
    border:solid 1px #FFFFFF;
}


.modern,
.button.modern,
input[type="button"].button.modern,
input[type="reset"].button.modern,
input[type="submit"].button.modern{
    background:#000;
    border:solid 1px #000;
}

.button.purple,
input[type="button"].button.purple,
input[type="reset"].button.purple,
input[type="submit"].button.purple{
    background:#3F4EAC;
    border:solid 1px #3F4EAC;
}

.notify{
    font-size:17px;
	margin-bottom:23px;
	padding:10px 20px;
	border:solid 1px #617078;
	background:#fff;
	border-radius: 3px;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
}

.tiny{
    font-size:11px;
    font-family:Roboto !important;
}

.button.small,
input[type="button"].button.small,
input[type="reset"].button.small,
input[type="submit"].button.small{
    padding:15px 19px !important;
}

.button.small,
input[type="button"].button.small,
input[type="reset"].button.small,
input[type="submit"].button.small{
    font-size: 10px;
    padding: 10px 16px !important;
}

.button.tiny{
    padding: 2px 3px !important;
}

.button.bare,
input[type="button"].button.bare,
input[type="reset"].button.bare,
input[type="submit"].button.bare{
    color:#000;
    border:solid 2px #000;
    background: #fff;
    font-family: roboto-medium !important;
    -webkit-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    -moz-box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
    box-shadow: 0px 3px 5px 0px rgba(179,179,179,0.43) !important;
}

strong{
    font-family:roboto-bold !important;
}

.left-float{
    float:left;
}

.right-float{
    float:right;
}

.tiny,
.information,
.instructions{
    font-size:12px;
    font-family:roboto-light !important;
}

.tiny-tiny{
    font-size:10px;
}

label{
    font-size:19px;
    margin:20px 0px;
    line-height:1.62em;
}

.href-dotted,
.href-dotted-black{
    font-family: roboto-slab !important;
}

.href-dotted{
    text-decoration: none !important;
    border-bottom:dotted 2px #428bca !important;
}

.href-dotted-black{
    color: #17161b !important;
    text-decoration: none !important;
    border-bottom:dotted 2px #17161b !important;
}

.fun{
    font-family: roboto-black !important;
}

.bold{
    font-family: roboto-bold !important;
}

.medium{
    font-family: roboto-medium !important;
}

.regular{
    font-family: roboto !important;
}

.thin{
    font-family: roboto-thin !important;
}

.lightf{
    font-family: roboto-light !important;
}


a{
    font-size:19px;
    color:#03afff;
    color:#13B1FF;
}

a:hover{
    cursor:hand;
    color:#03afff;
    color:#13B1FF
}


input[type="text"],
input[type="email"],
input[type="password"],
input[type="text"]:hover,
input[type="password"]:hover,
input[type="text"]:focus,
input[type="password"]:focus,
input[type="text"]:active,
input[type="password"]:active{
    color:#000;
    font-family:roboto-light !important;
    font-size:21px;
    background:#edf7ff;
    background:#ffffea;
    background:#fdff82;
    background:#f8f8f8;
    background:#fff;
    line-height:1.0em !important;
    padding:12px 12px !important;
    border:solid 1px #C9DCDC !important;
    -webkit-border-radius: 3px !important;
    -moz-border-radius: 3px !important;
    border-radius: 3px !important;
}

input::placeholder{
    font-size:19px;
}

input:-webkit-autofill,
input:-webkit-autofill:hover,
input:-webkit-autofill:focus,
textarea:-webkit-autofill,
textarea:-webkit-autofill:hover,
textarea:-webkit-autofill:focus,
select:-webkit-autofill,
select:-webkit-autofill:hover,
select:-webkit-autofill:focus {
    border:solid 1px #C9DCDC !important;
    -webkit-text-fill-color: #000;
    -webkit-box-shadow: 0 0 0px 1000px #fff inset;
    transition: background-color 5000s ease-in-out 0s;
}

.clear{
    clear:both;
}


body.obey{}