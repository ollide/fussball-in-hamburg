<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Weitere Städte</h1>

    <div>
    <#list cities as city>
        <p>
            <a href="/stadt/${city.key}">${city.name}</a>
        </p>
    </#list>
    </div>

</div>
</@layout.default>
