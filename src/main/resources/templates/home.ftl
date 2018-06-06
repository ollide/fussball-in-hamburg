<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in Hamburg</h1>
    <p class="subtitle">work in progress.</p>

    <table class="table">
        <tbody>
        <#list matchDays as matchDay>
        <tr>
            <td colspan="6">
                <h2 class="subtitle" style="color: black; margin-top: 10px">${matchDay.formattedDay()}</h2>
            </td>
        </tr>
            <#list matchDay.matches as match>
            <tr>
                <td>${match.formattedTime()}</td>
                <td>${match.clubHome}</td>
                <td>${match.clubAway}</td>
                <td>${match.league}</td>
                <td>${match.teamType}</td>
                <td><a href="${match.url}" target="_blank">Zum Spiel</a></td>
            </tr>
            </#list>
        </#list>
        </tbody>
    </table>
</div>

</@layout.default>
