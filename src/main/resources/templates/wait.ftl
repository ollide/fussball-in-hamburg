<#import "layout/default.ftl" as layout>
<@layout.default>

<div class="container">
    <h1 class="title">Fußball in ${city!"Hamburg"}</h1>

    <div class="columns">
        <div class="column is-one-quarter-desktop is-one-third-tablet">

            <p class="subtitle">Spiele werden geladen…</p>

            <div class="sk-folding-cube">
                <div class="sk-cube1 sk-cube"></div>
                <div class="sk-cube2 sk-cube"></div>
                <div class="sk-cube4 sk-cube"></div>
                <div class="sk-cube3 sk-cube"></div>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">
    setTimeout(location.reload.bind(location), 5000);
</script>

</@layout.default>
