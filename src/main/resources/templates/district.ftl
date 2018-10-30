<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Weitere Landkreise</h1>

    <div>
    <#list districts as district>
        <p>
            <a href="/kreis/${district.key}">${district.name}</a>
        </p>
    </#list>
    </div>

</div>
</@layout.default>
