<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">FIH Spezial</h1>

    <div>
    <#list specials as special>
        <p>
            <a href="/spezial/${special.key}">${special.name}</a>
        </p>
    </#list>
    </div>

</div>
</@layout.default>
