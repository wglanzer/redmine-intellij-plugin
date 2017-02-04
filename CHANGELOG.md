# Change Log
All notable changes to this project will be documented in this file.

This project versioning is based on [Semantic Versioning](http://semver.org/).

## [0.3] - 2017-02-04
### Changed
- \#4: Is the RedmineTopComponent really needed?
- Propertly ( https://github.com/aditosoftware/propertly ) as DataModel-Framework for settings (replaced Java-Beans)

### Fixed
- \#16: Notifications can be above others
- \#9: If config is reloaded, all active requests should be cancelled

## [0.2] - 2016-12-14
### Added
- \#10: Integrate travis-ci
- Versioning: SemVer

### Changed
- Notifiers now static
- Most Listeners are now weak
- ProjectListeners and TicketListeners -> Properties as constants
- Upgraded IJ-Dependencies to 2016.3.1
- \#2: NotifyBalloon not fancy enough

### Fixed
- \#6: Notifier notifies too much

## [0.1.2_1213160835] - 2016-12-13
### Fixed
- Server does not use "source.isCheckCertificate()"

## [0.1.1_1212162324] - 2016-12-12
### Added
- "checkCertificate"-Flag for every ISource

### Fixed
- Settings can't be saved to disk

## [0.1_1212160004] - 2016-12-12
### Added
- General base functionality

[0.3]: https://github.com/wglanzer/redmine-intellij-plugin/releases/tag/v0.3
[0.2]: https://github.com/wglanzer/redmine-intellij-plugin/releases/tag/v0.2
[0.1.2_1213160835]: https://github.com/wglanzer/redmine-intellij-plugin/releases/tag/0.1.2_1213160835
[0.1.1_1212162324]: https://github.com/wglanzer/redmine-intellij-plugin/releases/tag/0.1.1_1212162324
[0.1_1212160004]: https://github.com/wglanzer/redmine-intellij-plugin/releases/tag/0.1_1212160004