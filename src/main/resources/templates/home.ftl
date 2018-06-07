<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in Hamburg</h1>
    <p class="subtitle">die spiele der nächsten woche.</p>

    <div class="buttons has-addons team-type-filter">
        <span class="button is-active-filter" onclick="toggleTeamTypeFilter(this)">Herren</span>
        <span class="button is-active-filter" onclick="toggleTeamTypeFilter(this)">Frauen</span>
        <span class="button" onclick="toggleTeamTypeFilter(this)">A-Junioren</span>
        <span class="button" onclick="toggleTeamTypeFilter(this)">A-Juniorinnen</span>
    </div>

    <div class="buttons has-addons league-filter">
        <span class="button is-active-filter" onclick="toggleLeagueFilter(this)">Verbandsliga</span>
        <span class="button is-active-filter" onclick="toggleLeagueFilter(this)">Landesliga</span>
        <span class="button is-active-filter" onclick="toggleLeagueFilter(this)">Bezirksliga</span>
        <span class="button" onclick="toggleLeagueFilter(this)">Kreisliga</span>
        <span class="button" onclick="toggleLeagueFilter(this)">Kreisklasse</span>
        <span class="button is-active-filter" onclick="toggleLeagueFilter(this)">FS</span>
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
                        <i class="fas fa-external-link-alt"></i>
                    </a>
                </td>
            </tr>
            </#list>
        </#list>
        </tbody>
    </table>
</div>

<script type="application/javascript">
    ACTIVE_FILTER = 'is-active-filter';

    function init() {
        var teamTypeFilter = document.getElementsByClassName('team-type-filter')[0];
        var teamTypeFilterBtns = teamTypeFilter.getElementsByClassName('button');
        for (var i = 0; i < teamTypeFilterBtns.length; i++) {
            var teamTypeFilterBtn = teamTypeFilterBtns[i];
            if (!teamTypeFilterBtn.classList.contains(ACTIVE_FILTER)) {
                toggleByTeamType(false, teamTypeFilterBtn.innerText);
            }
        }

        var leagueFilter = document.getElementsByClassName('league-filter')[0];
        var leagueFilterBtns = leagueFilter.getElementsByClassName('button');
        for (var j = 0; j < leagueFilterBtns.length; j++) {
            var leagueFilterBtn = leagueFilterBtns[j];
            if (!leagueFilterBtn.classList.contains(ACTIVE_FILTER)) {
                toggleByLeague(false, leagueFilterBtn.innerText);
            }
        }
    }
    init();

    function toggleTeamTypeFilter(btn) {
        var active = btn.classList.toggle(ACTIVE_FILTER);
        var type = btn.innerText;
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
        var type = btn.innerText;
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
