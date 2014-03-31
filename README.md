KinoOST
=======

## Небольшие правила репозитория.

* Каждый работает в ветке по своей фамилии над задачей, после выполнения задачи ветка сливается в мастер, стирается, и заново почкуется от мастера при необходимости, для реализации следуюущей задачи.
* В мастер пушить категорически не рекомендуется

## Краткий FAQ по командам:

* `git pull origin branchname` -- подтягивает ветку из удаленного репозитория и сливает с локальной
* `git add dir1 dir2` -- помечает файлы для послудющего коммита, `git add .` -- внесет все изменения от корня
* `git commit -m "message"` -- вносит локальный коммит в репозиторий с сообщением `message`
* `git push origin branchname` -- пушит изменения на удаленный репозиторий в ветку `branchname`
* `git branch example` -- создает ветку с именем `example`, почкует от той, в которой находится пользователь при исопльзовании команды
* `git checkout branchname` -- переключиться на ветку `branchname`

Более подробные команды доступны на <http://git-scm.com/book/commands>


@TODO
--------
* Реализовать авторизацию через VK API (получение токена об успешной авторизации)
* Реализовать прослушивание аудио с помощью VK API
* Создать главную страницу приложения в соответсвии с разработанным ранее дизайном
* Создать страницу саундтрека и фильма в соответсвии с разработанным ранее дизайном
* Создать страницу избранного в соответсвии с разработанным ранее дизайном
* ~~Написать классы, соответсвующие сущностям локальной БД [реализовано]~~
* ~~Реализовать интерфейс взаимодействия с локальной СУБД SQLite (вынести все запросы в репозитории) [реализовано]~~
* ~Реализовать интерфейс взаимодействия приложения с API сервера~
* Реализовать infinite scrolling для отображения результатов поиска
* ~Реализовать полнотекстовый поиск по локальной БД~
* Реализовать плеер
