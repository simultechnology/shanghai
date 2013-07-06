
window.onload = function(){

  var path = window.location.pathname.match(/^\/\w*/)[0];
  var exit = false;
  $('div.navbar-inner li').each(function(idx, li) {
    if (exit) {
      return false;
    }
    var linkTags = $(li).find('a');
    var ret = $(linkTags).each(function(i, a) {
      if ($(a).attr('href').indexOf(path) > -1) {
        $(li).addClass('active');
        exit = true;
        return false;
      }
    });
  });

}
