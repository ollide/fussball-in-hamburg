<#macro default title="FussiFinder Hamburg">
<!DOCTYPE html>
<html lang="de">
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="keywords" content="Fußball,Amateurfußball,Football,Groundhopping,Groundhopper,Hamburg">
    <meta name="Description" content="Fußballspiele der nächsten Woche in Hamburg">

    <link rel="icon" type="image/png" href="favicon.png">

    <noscript>
        <link href="bulma.min.css" rel="stylesheet" type="text/css"/>
        <link href="bulma.override.css" rel="stylesheet" type="text/css"/>
        <link href="iconfont.css" rel="stylesheet" type="text/css"/>
    </noscript>

    <title>${title}</title>
</head>

<body>

    <#include "header.ftl"/>

<section class="section">

    <#nested/>

</section>

    <#include "footer.ftl"/>

    <#include "deferred_css.ftl"/>

</body>
</html>
</#macro>
