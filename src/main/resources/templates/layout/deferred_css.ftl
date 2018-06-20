<script type="text/javascript">
    var deferCss = document.getElementsByTagName('link')[0];

    var bulmaCss = document.createElement('link');
    bulmaCss.rel = 'stylesheet';
    bulmaCss.href = 'bulma.min.css';
    bulmaCss.type = 'text/css';
    deferCss.parentNode.insertBefore(bulmaCss, deferCss);

    var bulmaOverrideCss = document.createElement('link');
    bulmaOverrideCss.rel = 'stylesheet';
    bulmaOverrideCss.href = 'bulma.override.css';
    bulmaOverrideCss.type = 'text/css';
    deferCss.parentNode.insertBefore(bulmaOverrideCss, deferCss);

    var iconFont = document.createElement('link');
    iconFont.rel = 'stylesheet';
    iconFont.href = 'iconfont.css';
    iconFont.type = 'text/css';
    deferCss.parentNode.insertBefore(iconFont, deferCss);
</script>
