<#macro default title="FussiFinder Hamburg">
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="favicon.png">

    <link href="bulma.min.css" rel="stylesheet" type="text/css"/>
    <link href="bulma.override.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">

    <title>${title}</title>
</head>

<body>

    <#include "header.ftl"/>

<section class="section">

    <#nested/>

</section>

    <#--<#include "footer.ftl"/>-->

</body>
</html>
</#macro>
