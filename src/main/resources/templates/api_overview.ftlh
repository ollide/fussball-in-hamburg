<section id="matchcalendar">
    <#list zips as zip>
    <div class="match-calendar">
        <div class="matchlocation">
            <h3><a data-ajax-forced="{'plz':'${zip.getPlz()}'}" href="http://www.example.org/url">
                Hamburg (${zip.getPlz()})
            </a></h3>
        </div>
    </div>
    </#list>
</section>
