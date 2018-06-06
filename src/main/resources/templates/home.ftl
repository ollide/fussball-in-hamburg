<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in Hamburg</h1>
    <p class="subtitle">work in progress.</p>

    <#list matchDays as matchDay>
    <h2 class="subtitle">${matchDay.formattedDay()}</h2>
    <table class="table">
        <tbody>
        <tr>
        <#list matchDay.matches as match>
            <td>${match.formattedTime()}</td>
            <td>${match.clubHome}</td>
            <td>${match.clubAway}</td>
            <td>${match.league}</td>
            <td>${match.teamType}</td>
            <td><a href="${match.url}" target="_blank">Zum Spiel</a></td>
        </tr>
        </#list>
        </tbody>
    </table>
    </#list>
</div>

</@layout.default>
