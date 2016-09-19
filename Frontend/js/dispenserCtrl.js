var Dispenser = function () {

    ///*
    // Esta função é usada limpar a tela, voltando os controles para os valores padrão
    ///*
    var limparFormulario = function () {
        $('#id').val(''),
        $('#nomeDispenser').val(''),
        $('#codigo').val(''),
        $('#carteirinhaAssociado').val(''),

        $('#codigoMedicamento1').val('')
        $('#descricaoMedicamento1').val('');
        $('#validadeMedicamento1').val('');

        $('#codigoMedicamento2').val('')
        $('#descricaoMedicamento2').val('');
        $('#validadeMedicamento2').val('');

        $('#codigoMedicamento3').val('')
        $('#descricaoMedicamento3').val('');
        $('#validadeMedicamento3').val('');

        $('#codigoMedicamento4').val('')
        $('#descricaoMedicamento4').val('');
        $('#validadeMedicamento4').val('');
    };

    ///*
    // Esta função é usada para salvar as informações do dispenser no banco de dados
    ///*
    var salvarItem = function () {

        /// criação do objeto que deve ser persistido no serviço
        /// esta estrutura de dados JSON recebe os valores dos controles na tela
        var item = {
            id: $('#id').val(),
            nomeDispenser: $('#nomeDispenser').val(),
            codigo: $('#codigo').val(),
            carteirinhaAssociado: $('#carteirinhaAssociado').val(),
            dataCadastro: new Date(),
            validadeMedicamento1: new Date(),
            validadeMedicamento2: new Date(),
            validadeMedicamento3: new Date(),
            validadeMedicamento4: new Date(),
            codigoMedicamento1: 1,
            codigoMedicamento2: 2,
            codigoMedicamento3: 3,
            codigoMedicamento4: 4
        };

        if($('#descricaoMedicamento1').val()){
            item.descricaoMedicamento1 = $('#descricaoMedicamento1').val();            
        }

        if($('#descricaoMedicamento2').val()){
            item.descricaoMedicamento2 = $('#descricaoMedicamento2').val();            
        }

        if($('#descricaoMedicamento3').val()){
            item.descricaoMedicamento3 = $('#descricaoMedicamento3').val();                        
        }

        if($('#descricaoMedicamento4').val()){
            item.descricaoMedicamento4 = $('#descricaoMedicamento4').val();            
        }

        if (item === null) {
            return;
        };

        $.ajax({
            async: true,
            type: "PUT",
            data: JSON.stringify(item),
            url: API_URL + '/dispenser/v1/update',
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                limparFormulario();
                bootbox.alert('informações atualizadas com sucesso!');
            },
            error: function (xhr) {
                bootbox.alert(xhr.responseJSON.error.message);
            }
        });

    }

    ///*
    // Esta função é usada para carregar os valores da agenda nos controles da tela
    // Ele deve requisitar um incidente usando o valor do id que está no atributo 'id-objeto'
    ///*
    var editarItem = function () {

        // neste caso $this é o link que foi clicado
        var $this = $(this);
        
        // recupera o valor do id-objeto
        var $id = $this.attr('id-objeto');

        $.ajax({
            async: true,
            type: "GET",
            url: API_URL + '/dispenser/v1/get/' + $id,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                
                // quando o serviço recupera com sucesso um objeto agenda
                // o valor da variável 'data' virá com os dados do objeto
                // em seguida usamos os valores para aprentar nos controles da tela                
                $('#nomeDispenser').val(data.nomeDispenser);
                $('#id').val(data.id);
                $('#codigo').val(data.codigo);
                $('#carteirinhaAssociado').val(data.carteirinhaAssociado);

                $('#codigoMedicamento1').val(data.codigoMedicamento1);
                $('#descricaoMedicamento1').val(data.descricaoMedicamento1);
                $('#validadeMedicamento1').val(Utils.formatarData(data.validadeMedicamento1));

                $('#codigoMedicamento2').val(data.codigoMedicamento2);
                $('#descricaoMedicamento2').val(data.descricaoMedicamento2);
                $('#validadeMedicamento2').val(Utils.formatarData(data.validadeMedicamento2));

                $('#codigoMedicamento3').val(data.codigoMedicamento3);
                $('#descricaoMedicamento3').val(data.descricaoMedicamento3);
                $('#validadeMedicamento3').val(Utils.formatarData(data.validadeMedicamento3));

                $('#codigoMedicamento4').val(data.codigoMedicamento4);
                $('#descricaoMedicamento4').val(data.descricaoMedicamento4);
                $('#validadeMedicamento4').val(Utils.formatarData(data.validadeMedicamento4));

            },
            error: function (xhr) {
                bootbox.alert(xhr.responseJSON.error.message);
            }
        });
    };
     
    return {
        //Função principal que inicializa o módulo
        inicializar: function () {
            $('#btn-salvar').click(salvarItem);
            $('#btn-bh').click(editarItem);
            $('#btn-campinas').click(editarItem);
        }
    };
} ();