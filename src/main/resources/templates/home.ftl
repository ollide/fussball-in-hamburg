<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in Hamburg</h1>
    <p class="subtitle">work in progress.</p>

    <table class="table">
        <tbody>
        <#list matches as match>
        <tr>
            <td>${match.date}</td>
            <td>${match.clubHome}</td>
            <td>${match.clubAway}</td>
            <td>${match.league}</td>
            <td>${match.teamType}</td>
            <td><a href="${match.url}" target="_blank">Zum Spiel</a></td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>

</@layout.default>
