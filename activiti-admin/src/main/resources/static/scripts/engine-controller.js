/*
 * Copyright 2005-2015 Alfresco Software, Ltd. All rights reserved.
 * License rights for this program may be obtained from Alfresco Software, Ltd.
 * pursuant to a written agreement and any use of this program without such an
 * agreement is prohibited.
 */
'use strict';

/* Controllers */

activitiAdminApp.controller('EngineController', ['$rootScope', '$scope', '$http', '$timeout', '$modal', '$translate', '$q', 'gridConstants',
    function ($rootScope, $scope, $http, $timeout, $modal, $translate, $q, gridConstants) {

        // Set root navigation
        $rootScope.navigation = { selection: 'engine' };

        // Static data
        $scope.options = {
            schemaUpdate: ['true', 'false'],
            history: ['none', 'activity', 'audit', 'full']
        };

        // Empty model
        $scope.model = {};
        $scope.configIsValid = false;

        // Show popup to edit the Activiti endpoint
        $scope.editEndpointConfig = function () {
            if ($scope.activeServer) {
                showEditpointConfigModel($scope.activeServer);
            } else {
                // load default endpoint configs properties
                $http({ method: 'GET', url: '/rest/server-configs/default' }).
                    success(function (defaultServerconfig, status, headers, config) {
                        defaultServerconfig.clusterConfigId = $scope.activeCluster.id;
                        showEditpointConfigModel(defaultServerconfig);
                    });
            }

            function showEditpointConfigModel(server) {
                var cloneOfModel = {};
                for (var prop in server) {
                    cloneOfModel[prop] = server[prop];
                }

                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-edit-endpoint-popup.html',
                    controller: 'EditEndpointConfigModalInstanceCrtl',
                    resolve: {
                        server: function () { return cloneOfModel; }
                    }
                });

                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.ENDPOINT-UPDATED', result), 'info');
                        $rootScope.activeServer = result;
                    }
                });
            }
        };

        $scope.checkEndpointConfig = function () {
            $http({ method: 'GET', url: '/rest/activiti/engine-info', ignoreErrors: true }).
                success(function (data, status, headers, config) {
                    $scope.addAlert($translate.instant('ALERT.ENGINE.ENDPOINT-VALID', data), 'info');
                }).error(function (data, status, headers, config) {
                    $scope.addAlert($translate.instant('ALERT.ENGINE.ENDPOINT-INVALID', $rootScope.activeServer), 'error');
                });
        };

        $scope.checkDatabaseConfig = function () {
            if ($scope.selectedDatabaseConfig) {
                $http({ method: 'GET', url: '/rest/database-configs/healthCheck/' + $scope.selectedDatabaseConfig[0].id, ignoreErrors: true }).
                    success(function (data, status, headers, config) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-VALID', data), 'info');
                    }).error(function (data, status, headers, config) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-INVALID'), 'error');
                    });
            } else {
                $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-INVALID'), 'error');
            }
        }

        $scope.activateDatabaseConfig = function () {
            if ($scope.selectedDatabaseConfig) {
                $http({ method: 'GET', url: '/rest/database-configs/healthCheck/' + $scope.selectedDatabaseConfig[0].id, ignoreErrors: true }).
                    success(function (data, status, headers, config) {

                        var configToDiactiviate = {};
                        $scope.databaseConfigData.forEach((data) => {
                            if (data.active == true) {
                                configToDiactiviate = data;
                                configToDiactiviate.active = false;
                            }
                        })
                        $scope.selectedDatabaseConfig[0].active = true;
                        $http({ method: 'PUT', url: '/rest/database-configs/' + configToDiactiviate.id, data: configToDiactiviate })
                        $http({ method: 'PUT', url: '/rest/database-configs/' + $scope.selectedDatabaseConfig[0].id, data: $scope.selectedDatabaseConfig[0] })
                            .success(function (data, status, headers, config) {
                                $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-ACTIVATED'), 'info');
                                //$rootScope.loadDatabaseConfig();   // reload after change
                            }).
                            error(function (data, status, headers, config) {
                                $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-ACTIVATION-FAILED'), 'error');
                            });
                    }).
                    error(function (data, status, headers, config) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-ACTIVATION-IMPOSSIBLE'), 'error');
                    });

            }
        }

        // Show popup to add the database config
        $scope.addDatabaseConfig = function () {
            if ($rootScope.databaseConfigData) {
                showAddDatabaseConfigModel($rootScope.databaseConfigData);
            } else {
                // reload selected functional database config properties
                $http({ method: 'GET', url: '/rest/database-configs' }).
                    success(function (selectedDatabaseConfigData, status, headers, config) {
                        if (selectedDatabaseConfigData) {

                            selectedDatabaseConfigData = $scope.selectedDC;
                            showAddDatabaseConfigModel(selectedDatabaseConfigData);
                        } else {
                            showAddDatabaseConfigModel();
                        }
                    });
            }

            function showAddDatabaseConfigModel(databaseConfigData) {
                var cloneOfModel = {};
                for (var prop in databaseConfig) {
                    cloneOfModel[prop] = databaseConfigData[prop];
                }

                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-add-functional-database-config-popup.html',
                    controller: 'addDatabaseConfigModalInstanceCrtl',
                    resolve: {
                        databaseConfigData: function () { return cloneOfModel; }
                    }
                });

                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-ADDED', result), 'info');
                        $rootScope.loadDatabaseConfig();   // reload after change
                    }
                });
            }
        };
        // update selected functional database config
        $scope.updateSelectedDatabaseConfig = function () {
            if ($scope.selectedDatabaseConfig && $scope.selectedDatabaseConfig.length > 0) {
                var selectedDatabaseConfig = $scope.selectedDatabaseConfig[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-update-database-popup.html',
                    controller: 'updateDatabaseConfigModalInstanceCrtl',
                    resolve: {
                        dc: function () {
                            return selectedDatabaseConfig;
                        }
                    }
                });

                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedDatabaseConfig.splice(0, $scope.selectedDatabaseConfig.length);
                        $scope.addAlert($translate.instant('ALERT.ENGINE.DATABASE-UPDATED', updated), 'info');
                        $scope.loadDatabaseConfig();
                    }
                });
            }
        };

        // delete selected functional database config
        $scope.deleteSelectedDatabaseConfig = function () {
            if ($scope.selectedDatabaseConfig && $scope.selectedDatabaseConfig.length > 0) {
                var selectedDatabaseConfig = $scope.selectedDatabaseConfig[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-delete-functional-database-popup.html',
                    controller: 'deleteDatabaseConfigModalInstanceCrtl',
                    resolve: {
                        dc: function () {
                            return selectedDatabaseConfig;
                        }
                    }
                });
                
                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedDatabaseConfig.splice(0, $scope.selectedDatabaseConfig.length);
                        $scope.loadDatabaseConfig();
                    }
                });
            }
        };

        // load Functional databaseConfig properties
        $q.all([$translate('DATABASE.POPUP.EDIT-DATABASE.DATABASE-URL'),
        $translate('DATABASE.POPUP.EDIT-DATABASE.DATABASE-SCHEMA'),
        $translate('DATABASE.POPUP.EDIT-DATABASE.DATABASE-USERNAME')])

            .then(function (headers) {

                $scope.selectedDatabaseConfig = [];

                // Config for grid
                $scope.gridFunctionalDatabaseConfig = {
                    data: 'databaseConfigData',
                    enableRowReordering: true,
                    multiSelect: false,
                    keepLastSelected: false,
                    enableSorting: false,
                    rowHeight: 36,
                    selectedItems: $scope.selectedDatabaseConfig,
                    //afterSelectionChange: $scope.selectedDatabaseConfig,
                    columnDefs: [
                        { field: 'databaseUrl', displayName: headers[0], cellTemplate: gridConstants.defaultTemplate, width: '50%' },
                        { field: 'databaseSchema', displayName: headers[1], cellTemplate: gridConstants.defaultTemplate, width: '25%' },
                        { field: 'databaseUsername', displayName: headers[2], cellTemplate: gridConstants.defaultTemplate, width: '25%' }
                    ]
                };
            });

        // Show popup to add the Functional mapping
        $scope.addFunctionalMapping = function () {
            if ($rootScope.functionalMappingData) {
                showAddFunctionalMappingModel($rootScope.functionalMappingData);
            } else {
                // reload selected functional mapping properties
                $http({ method: 'GET', url: '/rest/functional-mapping' }).
                    success(function (selectedFunctionalMappingData, status, headers, config) {
                        if (selectedFunctionalMappingData) {

                            selectedFunctionalMappingData = $scope.selectedFM;
                            showAddFunctionalMappingModel(selectedFunctionalMappingData);
                        } else {
                            showAddFunctionalMappingModel();
                        }
                    });
            }

            function showAddFunctionalMappingModel(functionalMappingData) {
                var cloneOfModel = {};
                for (var prop in functionalMapping) {
                    cloneOfModel[prop] = functionalMappingData[prop];
                }

                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-add-functional-mapping-popup.html',
                    controller: 'addFunctionalMappingModalInstanceCrtl',
                    resolve: {
                        functionalMappingData: function () { return cloneOfModel; }
                    }
                });

                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.FUNCTIONAL-MAPPING-ADDED', result), 'info');
                        $rootScope.loadFunctionalMapping();   // reload after change
                    }
                });
            }
        };

        // update selected functional mapping
        $scope.updateSelectedFunctionalMapping = function () {
            if ($scope.selectedFunctionalMapping && $scope.selectedFunctionalMapping.length > 0) {
                var selectedFunctionalMapping = $scope.selectedFunctionalMapping[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-update-functional-mapping-popup.html',
                    controller: 'updateMappingModalInstanceCrtl',
                    resolve: {
                        fm: function () {
                            return selectedFunctionalMapping;
                        }
                    }
                });

                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedFunctionalMapping.splice(0, $scope.selectedFunctionalMapping.length);
                        $scope.addAlert($translate.instant('ALERT.ENGINE.FUNCTIONAL-MAPPING-UPDATED', updated), 'info');
                        $scope.loadFunctionalMapping();
                    }
                });
            }
        };

        // delete selected functional mapping
        $scope.deleteSelectedFunctionalMapping = function () {
            if ($scope.selectedFunctionalMapping && $scope.selectedFunctionalMapping.length > 0) {
                var selectedFunctionalMapping = $scope.selectedFunctionalMapping[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-delete-functional-mapping-popup.html',
                    controller: 'deleteMappingModalInstanceCrtl',
                    resolve: {
                        fm: function () {
                            return selectedFunctionalMapping;
                        }
                    }
                });

                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedFunctionalMapping.splice(0, $scope.selectedFunctionalMapping.length);
                        $scope.loadFunctionalMapping();
                    }
                });
            }
        };

        // load Functional Mapping properties
        $q.all([$translate('FUNCTIONAL-MAPPING.NAME'),
        $translate('FUNCTIONAL-MAPPING.PROCESSDEFINITIONKEY'),
        $translate('FUNCTIONAL-MAPPING.SQLQUERY')])

            .then(function (headers) {

                $scope.selectedFunctionalMapping = [];

                // Config for grid
                $scope.gridFunctionalMapping = {
                    data: 'functionalMappingData',
                    enableRowReordering: true,
                    multiSelect: false,
                    keepLastSelected: false,
                    enableSorting: false,
                    rowHeight: 36,
                    selectedItems: $scope.selectedFunctionalMapping,
                    //afterSelectionChange: $scope.selectedFunctionalMapping,
                    columnDefs: [
                        { field: 'name', displayName: headers[0], cellTemplate: gridConstants.defaultTemplate },
                        { field: 'processDefinitionKey', displayName: headers[1], cellTemplate: gridConstants.defaultTemplate },
                        { field: 'sqlQuery', displayName: headers[2], cellTemplate: gridConstants.defaultTemplate }
                    ]
                };
            });

        // Show popup to add the Filter config
        $scope.addFilterConfig = function () {
            if ($rootScope.filtersConfigData) {
                showAddFilterConfigModel($rootScope.filtersConfigData);
            } else {
                // reload selected filter config properties
                $http({ method: 'GET', url: '/rest/filters-config' }).
                    success(function (selectedFiltersConfigData, status, headers, config) {
                        if (selectedFiltersConfigData) {

                            selectedFiltersConfigData = $scope.selectedFC;
                            showAddFilterConfigModel(selectedFiltersConfigData);
                        } else {
                            showAddFilterConfigModel();
                        }
                    });
            }

            function showAddFilterConfigModel(filtersConfigData) {
                var cloneOfModel = {};
                for (var prop in filterConfig) {
                    cloneOfModel[prop] = filtersConfigData[prop];
                }

                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-add-filter-config-popup.html',
                    controller: 'addFilterConfigModalInstanceCrtl',
                    resolve: {
                        filtersConfigData: function () { return cloneOfModel; }
                    }
                });

                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('ALERT.ENGINE.FILTER-CONFIG-ADDED', result), 'info');
                        $rootScope.loadFiltersConfig();   // reload after change
                    }
                });
            }
        };

        // update selected filter config
        $scope.updateSelectedFilterConfig = function () {
            if ($scope.selectedFiltersConfig && $scope.selectedFiltersConfig.length > 0) {
                var selectedFiltersConfig = $scope.selectedFiltersConfig[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-update-filter-config-popup.html',
                    controller: 'updateFilterConfigModalInstanceCrtl',
                    resolve: {
                        fc: function () {
                            return selectedFiltersConfig;
                        }
                    }
                });

                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedFiltersConfig.splice(0, $scope.selectedFiltersConfig.length);
                        $scope.addAlert($translate.instant('ALERT.ENGINE.FILTER-CONFIG-UPDATED', updated), 'info');
                        $scope.loadFiltersConfig();
                    }
                });
            }
        };

        // delete selected filter config
        $scope.deleteSelectedFilterConfig = function () {
            if ($scope.selectedFiltersConfig && $scope.selectedFiltersConfig.length > 0) {
                var selectedFiltersConfig = $scope.selectedFiltersConfig[0];
                var modalInstance = $modal.open({
                    templateUrl: 'views/engine-delete-filter-config-popup.html',
                    controller: 'deleteFilterConfigModalInstanceCrtl',
                    resolve: {
                        fc: function () {
                            return selectedFiltersConfig;
                        }
                    }
                });

                modalInstance.result.then(function (updated) {
                    if (updated == true) {
                        $scope.selectedFiltersConfig.splice(0, $scope.selectedFiltersConfig.length);
                        $scope.loadFiltersConfig();
                    }
                });
            }
        };

        // load Filters config properties
        $q.all([$translate('FILTERS-CONFIG.GRID.FILTER-NAME'),
        $translate('FILTERS-CONFIG.GRID.PROCESS-ID-COLUMN-NAME'),
        $translate('FILTERS-CONFIG.GRID.PROCESSDEFINITIONKEY'),
        $translate('FILTERS-CONFIG.GRID.SQLQUERY')])

            .then(function (headers) {

                $scope.selectedFiltersConfig = [];

                // Config for grid filters config
                $scope.gridFiltersConfig = {
                    data: 'filtersConfigData',
                    enableRowReordering: true,
                    multiSelect: false,
                    keepLastSelected: false,
                    enableSorting: false,
                    rowHeight: 36,
                    selectedItems: $scope.selectedFiltersConfig,
                    columnDefs: [
                        { field: 'filterName', displayName: headers[0], cellTemplate: gridConstants.defaultTemplate },
                        { field: 'processIdColumnName', displayName: headers[1], cellTemplate: gridConstants.defaultTemplate },
                        { field: 'processDefinitionKey', displayName: headers[2], cellTemplate: gridConstants.defaultTemplate },
                        { field: 'sqlQuery', displayName: headers[3], cellTemplate: gridConstants.defaultTemplate }
                    ]
                };
            });

    }]);


activitiAdminApp.controller('EditEndpointConfigModalInstanceCrtl',
    ['$scope', '$modalInstance', '$http', 'server', function ($scope, $modalInstance, $http, server) {

        $scope.model = { server: server };

        $scope.status = { loading: false };

        $scope.ok = function () {
            $scope.status.loading = true;

            delete $scope.model.error;

            var serverConfigUrl = '/rest/server-configs';
            var method = 'PUT';
            if ($scope.model.server && $scope.model.server.id) {
                serverConfigUrl += '/' + $scope.model.server.id;
            } else {
                method = 'POST';
            }

            $http({ method: method, url: serverConfigUrl, data: $scope.model.server }).
                success(function (data, status, headers, config) {
                    $scope.status.loading = false;
                    $modalInstance.close($scope.model.server);
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;
                    $scope.model.error = {
                        statusCode: status,
                        message: data
                    };
                });
        };

        $scope.cancel = function () {
            if (!$scope.status.loading) {
                $modalInstance.dismiss('cancel');
            }
        };
    }]);

activitiAdminApp.controller('addDatabaseConfigModalInstanceCrtl', ['$scope', '$rootScope', '$modalInstance', '$http', '$timeout', '$location', '$translate', '$q', 'gridConstants', 'databaseConfigData',
    function ($scope, $rootScope, $modalInstance, $http, $timeout, $location, $translate, $q, gridConstants, database) {

        $scope.model = { database: database };

        $scope.status = { loading: false };

        $scope.ok = function () {
            $scope.status.loading = true;

            delete $scope.model.error;

            var databaseConfigUrl = '/rest/database-configs';
            var method = 'PUT';

            if ($scope.model.database && $scope.model.database.id) {
                console.log('The database configuration already exist');
                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('The database configuration already exist', result), 'warning');
                    }
                });

            } else {
                method = 'POST';
            }

            $http({ method: method, url: databaseConfigUrl, data: $scope.model.database }).
                success(function (data, status, headers, config) {
                    $scope.status.loading = false;
                    $modalInstance.close($scope.model.database);
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });
        };

        $scope.cancel = function () {
            if (!$scope.status.loading) {
                $modalInstance.dismiss('cancel');
            }
        };
    }]);
activitiAdminApp.controller('updateDatabaseConfigModalInstanceCrtl', ['$rootScope', '$scope', '$modalInstance', '$http', '$timeout', 'dc',
    function ($rootScope, $scope, $modalInstance, $http, $timeout, dc) {
        $scope.status = { loading: false };
        $scope.originalDatabaseConfig = dc;
        $scope.model = { dc: dc };

        $scope.database = {
            id: dc.id,
            databaseUrl: dc.databaseUrl,
            databaseSchema: dc.databaseSchema,
            databaseUsername: dc.databaseUsername,
            databasePassword: dc.databasePassword,
            active: dc.active
        };

        $scope.ok = function () {

            $scope.status.loading = true;

            var dataForPut = {
                id: $scope.database.id,
                databaseUrl: $scope.database.databaseUrl,
                databaseSchema: $scope.database.databaseSchema,
                databaseUsername: $scope.database.databaseUsername,
                databasePassword: $scope.database.databasePassword,
                active: $scope.database.active
            };


            $http({ method: 'PUT', url: '/rest/database-configs/' + $scope.database.id, data: dataForPut }).
                success(function (data, status, headers, config) {
                    $modalInstance.close(true);
                    $scope.status.loading = false;
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        // load process Definitions latest keys
        $scope.loadLatestProcessDefinitions = function () {

            var promise = $http({
                method: 'GET',
                url: '/rest/activiti/process-definitions?size=100000000&latest=true'
            })

            return promise;
        }

        $scope.loadLatestProcessDefinitions().success(function (response) {
            var keyArray = new Array();
            response.data.forEach(function (processDefinition) {
                keyArray.push(processDefinition.key)
            });
            $scope.processDefinitionsKeys = keyArray;
        });
    }]);

activitiAdminApp.controller('deleteDatabaseConfigModalInstanceCrtl',
    ['$rootScope', '$scope', '$modalInstance', '$http', 'dc',
        function ($rootScope, $scope, $modalInstance, $http, dc) {

            $scope.status = { loading: false };
            $scope.deletedDatabaseConfig = dc;    
            $scope.deleteSelectedDatabaseConfig = function () {
                $http({ method: 'DELETE', url: '/rest/database-configs/' + $scope.deletedDatabaseConfig.id }).
                    success(function (data, status, headers, config) {
                        $modalInstance.close(true);
                        $scope.status.loading = false;
                    }).
                    error(function (data, status, headers, config) {
                        $modalInstance.close(false);
                        $scope.status.loading = false;
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
        
activitiAdminApp.controller('addFunctionalMappingModalInstanceCrtl', ['$scope', '$rootScope', '$modalInstance', '$http', '$timeout', '$location', '$translate', '$q', 'gridConstants', 'functionalMappingData',
    function ($scope, $rootScope, $modalInstance, $http, $timeout, $location, $translate, $q, gridConstants, functionalMappingData) {

        $scope.model = { functionalMappingData: functionalMappingData };

        $scope.status = { loading: false };

        $scope.ok = function () {
            $scope.status.loading = true;

            delete $scope.model.error;

            var functionalMappingUrl = '/rest/functional-mapping';
            var method = 'PUT';

            if ($scope.model.functionalMappingData && $scope.model.functionalMappingData.name) {
                console.log('the functional Mapping already exist');
                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('The functional Mapping already exist', result), 'warning');
                    }
                });

            } else {
                method = 'POST';
            }

            $http({ method: method, url: functionalMappingUrl, data: $scope.model.functionalMapping }).
                success(function (data, status, headers, config) {
                    $scope.status.loading = false;
                    $modalInstance.close($scope.model.functionalMapping);
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });
        };

        $scope.cancel = function () {
            if (!$scope.status.loading) {
                $modalInstance.dismiss('cancel');
            }
        };

        // load process Definitions latest keys
        $scope.loadLatestProcessDefinitions = function () {

            var promise = $http({
                method: 'GET',
                url: '/rest/activiti/process-definitions?size=100000000&latest=true'
            })

            return promise;
        }

        $scope.loadLatestProcessDefinitions().success(function (response) {
            var keyArray = new Array();
            response.data.forEach(function (processDefinition) {
                keyArray.push(processDefinition.key)
            });
            $scope.processDefinitionsKeys = keyArray;
        });
    }]);
    


activitiAdminApp.controller('updateMappingModalInstanceCrtl', ['$rootScope', '$scope', '$modalInstance', '$http', '$timeout', 'fm',
    function ($rootScope, $scope, $modalInstance, $http, $timeout, fm) {
        $scope.status = { loading: false };
        $scope.originalFunctionalMapping = fm;
        $scope.model = { fm: fm };

        $scope.updateFunctionalMapping = {
            id: fm.id,
            name: fm.name,
            processDefinitionKey: fm.processDefinitionKey,
            sqlQuery: fm.sqlQuery
        };

        $scope.ok = function () {

            $scope.status.loading = true;

            var dataForPut = {
                id: $scope.updateFunctionalMapping.id,
                name: $scope.updateFunctionalMapping.name,
                processDefinitionKey: $scope.updateFunctionalMapping.processDefinitionKey,
                sqlQuery: $scope.updateFunctionalMapping.sqlQuery

            };


            $http({ method: 'PUT', url: '/rest/functional-mapping/' + $scope.updateFunctionalMapping.id, data: dataForPut }).
                success(function (data, status, headers, config) {
                    $modalInstance.close(true);
                    $scope.status.loading = false;
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        // load process Definitions latest keys
        $scope.loadLatestProcessDefinitions = function () {

            var promise = $http({
                method: 'GET',
                url: '/rest/activiti/process-definitions?size=100000000&latest=true'
            })

            return promise;
        }

        $scope.loadLatestProcessDefinitions().success(function (response) {
            var keyArray = new Array();
            response.data.forEach(function (processDefinition) {
                keyArray.push(processDefinition.key)
            });
            $scope.processDefinitionsKeys = keyArray;
        });
    }]);

activitiAdminApp.controller('deleteMappingModalInstanceCrtl',
    ['$rootScope', '$scope', '$modalInstance', '$http', 'fm',
        function ($rootScope, $scope, $modalInstance, $http, fm) {

            $scope.status = { loading: false };
            $scope.deletedFunctionalMapping = fm;

            $scope.deleteSelectedFunctionalMapping = function () {
                $http({ method: 'DELETE', url: '/rest/functional-mapping/' + $scope.deletedFunctionalMapping.id }).
                    success(function (data, status, headers, config) {
                        $modalInstance.close(true);
                        $scope.status.loading = false;
                    }).
                    error(function (data, status, headers, config) {
                        $modalInstance.close(false);
                        $scope.status.loading = false;
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);

activitiAdminApp.controller('addFilterConfigModalInstanceCrtl', ['$scope', '$rootScope', '$modalInstance', '$http', '$timeout', '$location', '$translate', '$q', 'gridConstants', 'filtersConfigData',
    function ($scope, $rootScope, $modalInstance, $http, $timeout, $location, $translate, $q, gridConstants, filtersConfigData) {

        $scope.model = { filtersConfigData: filtersConfigData };

        $scope.status = { loading: false };

        $scope.ok = function () {
            $scope.status.loading = true;

            delete $scope.model.error;

            var filterConfigUrl = '/rest/filters-config';
            var method = 'PUT';

            if ($scope.model.functionalMappingData && $scope.model.functionalMappingData.name) {
                console.log('the filter config already exist');
                modalInstance.result.then(function (result) {
                    if (result) {
                        $scope.addAlert($translate.instant('The filter config already exist', result), 'warning');
                    }
                });

            } else {
                method = 'POST';
            }

            $http({ method: method, url: filterConfigUrl, data: $scope.model.filterConfig }).
                success(function (data, status, headers, config) {
                    $scope.status.loading = false;
                    $modalInstance.close($scope.model.filterConfig);
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });
        };

        $scope.cancel = function () {
            if (!$scope.status.loading) {
                $modalInstance.dismiss('cancel');
            }
        };

        // load process Definitions latest keys
        $scope.loadLatestProcessDefinitions = function () {

            var promise = $http({
                method: 'GET',
                url: '/rest/activiti/process-definitions?size=100000000&latest=true'
            })

            return promise;
        }

        $scope.loadLatestProcessDefinitions().success(function (response) {
            var keyArray = new Array();
            response.data.forEach(function (processDefinition) {
                keyArray.push(processDefinition.key)
            });
            $scope.processDefinitionsKeys = keyArray;
        });
    }]);

activitiAdminApp.controller('updateFilterConfigModalInstanceCrtl', ['$rootScope', '$scope', '$modalInstance', '$http', '$timeout', 'fc',
    function ($rootScope, $scope, $modalInstance, $http, $timeout, fc) {
        $scope.status = { loading: false };
        $scope.originalFilterConfig = fc;
        $scope.model = { fc: fc };

        $scope.updateFilterConfig = {
            id: fc.id,
            filterName: fc.filterName,
            processIdColumnName: fc.processIdColumnName,
            processDefinitionKey: fc.processDefinitionKey,
            sqlQuery: fc.sqlQuery
        };

        $scope.ok = function () {

            $scope.status.loading = true;

            var dataForPut = {
                id: $scope.updateFilterConfig.id,
                filterName: $scope.updateFilterConfig.filterName,
                processIdColumnName: $scope.updateFilterConfig.processIdColumnName,
                processDefinitionKey: $scope.updateFilterConfig.processDefinitionKey,
                sqlQuery: $scope.updateFilterConfig.sqlQuery

            };

            $http({ method: 'PUT', url: '/rest/filters-config/' + $scope.updateFilterConfig.id, data: dataForPut }).
                success(function (data, status, headers, config) {
                    $modalInstance.close(true);
                    $scope.status.loading = false;
                }).
                error(function (data, status, headers, config) {
                    $scope.status.loading = false;

                    $scope.model.error = {
                        statusCode: status,
                        message: data.message
                    };
                });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        // load process Definitions latest keys
        $scope.loadLatestProcessDefinitions = function () {

            var promise = $http({
                method: 'GET',
                url: '/rest/activiti/process-definitions?size=100000000&latest=true'
            })

            return promise;
        }

        $scope.loadLatestProcessDefinitions().success(function (response) {
            var keyArray = new Array();
            response.data.forEach(function (processDefinition) {
                keyArray.push(processDefinition.key)
            });
            $scope.processDefinitionsKeys = keyArray;
        });
    }]);

activitiAdminApp.controller('deleteFilterConfigModalInstanceCrtl',
    ['$rootScope', '$scope', '$modalInstance', '$http', 'fc',
        function ($rootScope, $scope, $modalInstance, $http, fc) {

            $scope.status = { loading: false };
            $scope.deletedFilterConfig = fc;

            $scope.deleteSelectedFilterConfig = function () {
                $http({ method: 'DELETE', url: '/rest/filters-config/' + $scope.deletedFilterConfig.id }).
                    success(function (data, status, headers, config) {
                        $modalInstance.close(true);
                        $scope.status.loading = false;
                    }).
                    error(function (data, status, headers, config) {
                        $modalInstance.close(false);
                        $scope.status.loading = false;
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);




