<div class="match-calendar">
    <div class="matchlocation">
        <h3>Hamburg</h3>
    </div>
    <div class="table-container">
        <table class="table">
            <thead>
            <tr></tr>
            </thead>
            <tbody>
            <#list matchDays as matchDay>

            <tr class="row-headline">
                <td colspan="11">${matchDay.formattedDay()}</td>
            </tr>
            <#list matchDay.matches as match>

            <tr class="odd">
                <td class="column-date">
                    <#if match?index == 0>
                        ${match.formattedDateTime()}
                    <#else>
                        ${match.formattedTime()}
                    </#if>
                </td>
                <td class="column-team">${match.teamType}</td>
                <td class="column-league">${match.league}</td>
                <td class="column-club">
                    <a href="http://www.example.org/team-url"
                       class="club-wrapper">
                        <div class="club-logo table-image">
                            <span data-responsive-image="//www.example.org/image-url"></span>
                        </div>
                        <div class="club-name">${match.clubHome}</div>
                    </a>
                </td>
                <td class="strong no-border no-padding">:</td>
                <td class="column-club">
                    <a href="http://www.example.org/team-url"
                       class="club-wrapper">
                        <div class="club-logo table-image">
                            <span data-responsive-image="//www.example.org/image-url"></span>
                        </div>
                        <div class="club-name">${match.clubAway}</div>
                    </a>
                </td>
                <td class="column-score">
                    <a href="http://www.example.org/match-url"><span
                            data-obfuscation="jxjitdxa" class="score-left">&#xE68F;</span><span
                            class="colon">:</span><span data-obfuscation="jxjitdxa" class="score-right">&#xE68F;</span></a>
                </td>
                <td class="column-detail">
                    <a href="http://www.example.org/match-url"><span
                            class="icon-angle-right hidden-full"></span><span class="visible-full">Match<span
                            class="icon-link-arrow"></span></span></a>
                </td>
            </tr>
            </#list>
            </#list>
            </tbody>
        </table>
    </div>
</div>
