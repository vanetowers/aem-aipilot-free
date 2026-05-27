(function() {
    function initHeroVideos() {
        var heroes = document.querySelectorAll('[data-cmp-is="hero"]');
        for (var i = 0; i < heroes.length; i++) {
            var hero = heroes[i];
            var video = hero.querySelector('video');
            if (video) {
                initVideo(hero, video);
            }
        }
    }

    function initVideo(hero, video) {
        var playPromise = video.play();
        if (playPromise !== undefined) {
            playPromise.catch(handlePlayError);
        }

        function handlePlayError() {
            function onClick() {
                video.play();
                hero.removeEventListener('click', onClick);
                hero.removeEventListener('touchstart', onClick);
            }
            hero.addEventListener('click', onClick);
            hero.addEventListener('touchstart', onClick);
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initHeroVideos);
    } else {
        initHeroVideos();
    }
})();
