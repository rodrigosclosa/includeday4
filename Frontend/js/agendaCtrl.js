var Agenda = function () {

    //Dispenser ids porque os valores estão fixos no código como BH e Campinas
    //var dispenserList = [{'value':1,'name':'BH'},{'value':2,'name':'Campinas'}];

    ///*
    // Esta função é usada limpar a tela, voltando os controles para os valores padrão
    ///*
    var limparFormulario = function () {
            $('#dispenser').val(''),
            $('#medicamento').val(''),
            $('#inicio').val('')
            $('#intervalo').val('');
            $('#dosagem').val('');
    };

    ///*
    // Esta função é usada para carregar os dispensers cadastrados no combobox    
    ///*
    var carregarListaDispensers = function () {
        $.ajax({
            async: true,
            type: "GET",
            url: API_URL + "/dispenser/v1/get/",
            dataType: "JSON",
            processData: true,
            success: function (data) {
                if (data != null && data.items != null && data.items.length > 0) {
                    $("#dispenser").empty();
                    var option = new Option('-- Selecione uma opção --','');
                    $('#dispenser').append(option);
                    $.each(data.items, function (index, item) {
                        option = new Option(item.nomeDispenser,item.id);
                        $('#dispenser').append(option);
                    });                    
                }
            },
            error: function (xhr) {
                alert("Ocorreu um erro ao carregar a lista de dispensers.");
            }
        });
    };

    ///*
    // Esta função é usada para recuperar o id do dispenser selecionado no combobox    
    ///*
    $("#dispenser").change(function(){
        if(this.value !== null && this.value !== 'undefined' && this.value !== ''){
            carregarListaMedicamentos(this.value);	
		}
		mudarEstadoComboBoxMedicamentos();		
    });
    
    ///*
    // Esta função é usada para carregar os medicamentos do dispenser selecionado no combobox    
    ///*
    var carregarListaMedicamentos = function (dispenserId) {
		$.ajax({
            async: true,
            type: "GET",
            url: API_URL + "/dispenser/v1/get/" + dispenserId,
            dataType: "JSON",
            processData: true,
            success: function (data) {
                if (data != null) {
                    $('#medicamento').empty();
                    var option = new Option('-- Selecione uma opção --','');
                    $("#medicamento").append(option);
                    $('#medicamento').removeAttr('disabled');
					for (var i=1;i<=4;i++){
						if (data['codigoMedicamento'+i] && data['descricaoMedicamento'+i]){
							option = new Option(data['descricaoMedicamento'+i], data['codigoMedicamento'+i]);
                            $('#medicamento').append(option);
                        }
                    }                                        
                }
			},
            error: function (xhr) {
                alert("Ocorreu um erro ao carregar a lista de medicamentos.");
            }
		});		
    };

	///*
    // Esta função é usada para habilitar ou desabilitar o combobox de medicamentos de acordo com o combobox de dispenser
    ///*
    var mudarEstadoComboBoxMedicamentos = function(){
		if($('#medicamento').length <= 1 || $('#dispenser').value === ''){
			$('#medicamento').attr('disabled',true);
			$('#medicamento').empty();
		}else{
			$('#medicamento').removeAttr('disabled');
		}
	} 
    
	///*
    // Esta função é usada para salvar a agenda no banco de dados do serviço
    ///*
    var cadastrarItem = function (latlong) {

        /// criação do objeto que deve ser persistido no serviço
        /// esta estrutura de dados JSON recebe os valores dos controles na tela
        var item = {
            idDispenser: $('#dispenser').val(),
            numeroMedicamento: $('#medicamento').val(),
            dataInicio: Utils.formatarDataJson($('#inicio').val()),
            intervaloMinutos: $('#intervalo').val(),
            dosagem: $('#dosagem').val()
        };

        if (item === null) {
            return;
        };

        $.ajax({
            async: true,
            type: "POST",
            data: JSON.stringify(item),
            url: API_URL + '/agenda/v1/new',
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                limparFormulario();
                carregarLista();
                bootbox.alert('Cadastrado com sucesso!');
            },
            error: function (xhr) {
                bootbox.alert(xhr.responseJSON.error.message);
            }
        });

    }

    ///*
    // Esta função é usada para remover o incidente no banco de dados do serviço
    // Ela precisa do 'id-objeto' que está no atributo do link
    ///*
    var removerItem = function () {
        
        var $this = $(this);
        var $id = $this.attr('id-objeto');

        // esta função cria um popup de confirmação para o usuário
        // permitindo que ele desista ou confirme a ação de remoção
        bootbox.confirm("Tem certeza que deseja remover?", function (result) {

            // Ao responder (sim ou não) no popup, o resultado é enviado para a variável 'result'
            // result = true se o usuário disse que SIM, confirma a ação
            // result = false se o usuário desistiu e disse que NÃO confirma a ação
            if (result) {
                $.ajax({
                    async: true,
                    type: "DELETE",
                    url: API_URL + '/agenda/v1/delete/' + $id,
                    dataType: "JSON",
                    processData: true,
                    success: function (data) {
                        carregarLista();
                        bootbox.alert('Removido com sucesso!');
                    },
                    error: function (xhr) {
                        bootbox.alert(xhr.responseJSON.error.message);
                    }
                });
            }
        });

    };
    
    ///*
    // Esta função é usada habilitar os eventos de click na tela
    // ela deve ser executada, assim que a tela estiver pronta para ser utilizada (geralmente no contrutor)      
    ///*
    var adicionarEventos = function () {

        $('.table-result .btn-default').each(function () {
            var $this = $(this);
            $this.click(editarItem);
        });

        $('.table-result .btn-danger').each(function () {
            var $this = $(this);
            $this.click(removerItem);
        });

    };
        
    ///*
    // Esta função é usada para carregar os medicamentos na lista da página (tabela)    
    ///*
    var carregarLista = function () {

        $.ajax({
            async: true,
            type: "GET",
            url: API_URL + '/agenda/v1/get/',
            dataType: "JSON",
            processData: true,
            success: function (data) {

                Utils.limparLista();

                if (data != null && data.items != null && data.items.length > 0) {

                    $.each(data.items, function (i, item) {

                        var tr = $('<tr/>');
                        tr.append("<td><h4>" + Utils.obterNomeDispenser(item.idDispenser) + "</h4></td>");
                        tr.append("<td><h4>" + item.medicamento.descricaoMedicamento + "</h4></td>");
                        tr.append("<td><h4>" + Utils.formatarData(item.dataInicio) + "</h4></td>");                        
                        tr.append("<td><h4>" + item.dosagem + "</h4></td>");
                        tr.append("<td><h4>" + Utils.converterTrueFalseEmTexto(item.dosagemRetirada) + "</h4></td>");
                        tr.append("<td><h4 style='text-align: center'>" + item.intervaloMinutos + " min</h4></td>");
                        
                        var template = "<td>";
                        template += "<div class='btn-group btn-group-md btn-group-show-label'>";
                        template += "<button type='button' name='botao-remover' id='botao-remover' id-objeto='" + item.id + "' class='btn btn-danger'><span class='glyphicon glyphicon glyphicon-remove' aria-hidden='true'> Remover</span></button>";
                        template += "</div>";
                        template += "</td>";

                        tr.append(template);   
                        $('.table-result').append(tr);                    

                    });
                    
                    // a função de adicionar eventos precisa ser chamada após a lista ser preenchida
                    // pois os links que foram criados precisam agora receber o comportamento de click
                    adicionarEventos();

                }else{
                    
                    var tr = $('<tr/>');
                    tr.append("<td colspan='7'><h2 style='text-align:center; margin-bottom: 30px'><span class='label label-default'>Não há nenhum medicamento cadastrado para este dispenser</span></h2></td>");                    
                    $('.table-result').append(tr);

                }

            },
            error: function (xhr) {
                alert("Ocorreu um erro ao carregar a lista.");
            }
        });

    };

    return {
        //Função principal que inicializa o módulo
        inicializar: function () {
            carregarListaDispensers();
            carregarLista();
            $('#btn-cadastrar').click(cadastrarItem);

            $('.datepicker').datepicker({
                format: 'dd/mm/yyyy',
                startDate: '-3d'
            });
        }
    };
} ();