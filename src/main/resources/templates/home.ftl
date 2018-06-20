<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in Hamburg</h1>
    <p class="subtitle">die ${stats.numberOfMatches} spiele der nächsten woche.</p>

    <div class="buttons has-addons team-type-filter">
    <#list teams as type>
        <#assign name = type.getName() />
        <span class="button <#if type.isActive()>is-active-filter</#if>"
              onclick="toggleTeamTypeFilter(this)" data-filter-name="${name}">
            ${name}
        </span>
    </#list>
    </div>

    <div class="buttons has-addons league-filter">
    <#list leagues as league>
        <#assign name = league.getName() />
        <span class="button <#if league.isActive()>is-active-filter</#if>"
              onclick="toggleLeagueFilter(this)" data-filter-name="${name}">
            ${name}
        </span>
    </#list>
    </div>

    <table class="table matches">
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
                <td class="team-league">${match.league}</td>
                <td class="team-type">${match.teamType}</td>
                <td>
                    <a href="${match.url}" target="_blank" rel="noreferrer">
                        <i class="icon-link-ext"></i>
                    </a>
                </td>
            </tr>
            </#list>
        </#list>
        </tbody>
    </table>
</div>

<script type="application/javascript">
    DATA_FILTER_NAME = 'data-filter-name';
    ACTIVE_FILTER = 'is-active-filter';

    function init() {
        var teamTypeFilter = document.getElementsByClassName('team-type-filter')[0];
        var teamTypeFilterBtns = teamTypeFilter.getElementsByClassName('button');
        for (var i = 0; i < teamTypeFilterBtns.length; i++) {
            var teamTypeFilterBtn = teamTypeFilterBtns[i];
            if (!teamTypeFilterBtn.classList.contains(ACTIVE_FILTER)) {
                toggleByTeamType(false, teamTypeFilterBtn.getAttribute(DATA_FILTER_NAME));
            }
        }

        var leagueFilter = document.getElementsByClassName('league-filter')[0];
        var leagueFilterBtns = leagueFilter.getElementsByClassName('button');
        for (var j = 0; j < leagueFilterBtns.length; j++) {
            var leagueFilterBtn = leagueFilterBtns[j];
            if (!leagueFilterBtn.classList.contains(ACTIVE_FILTER)) {
                toggleByLeague(false, leagueFilterBtn.getAttribute(DATA_FILTER_NAME));
            }
        }
    }
    init();

    function toggleTeamTypeFilter(btn) {
        var active = btn.classList.toggle(ACTIVE_FILTER);
        var type = btn.getAttribute(DATA_FILTER_NAME);
        toggleByTeamType(active, type);
    }

    function toggleByTeamType(active, type) {
        var tds = document.getElementsByClassName('team-type');
        for (var i = 0; i < tds.length; i++) {
            var td = tds[i];
            if (td.innerText === type) {
                var list = td.parentElement.classList;
                active ? list.remove('hidden-by-type') : list.add('hidden-by-type');
            }
        }
    }

    function toggleLeagueFilter(btn) {
        var active = btn.classList.toggle(ACTIVE_FILTER);
        var type = btn.getAttribute(DATA_FILTER_NAME);
        toggleByLeague(active, type);
    }

    function toggleByLeague(active, league) {
        var tds = document.getElementsByClassName('team-league');
        for (var i = 0; i < tds.length; i++) {
            var td = tds[i];
            var matchLeague = td.innerText;
            if (matchLeague === league || isFSfilter(league, matchLeague)) {
                var list = td.parentElement.classList;
                active ? list.remove('hidden-by-league') : list.add('hidden-by-league');
            }
        }
    }

    function isFSfilter(filter, matchLeague) {
        return filter === 'FS' && matchLeague.match(/[LBK]-FS/);
    }
</script>

</@layout.default>
