var voltarCard;
function selAbaTbl(voltar) {
	if (voltar == 'nao') {
		voltarCard = voltar
	}
	if (voltarCard == 'nao') {
 		 $('.nav-tab a[href="#visualizar"]').tab('show');
	} else {
		 $('.nav-tab a[href="#disponiveis"]').tab('show');
		 $('.nav-tab a[href="#visualizar"]').tab('show');
	}
}

function selAbaEdit(voltar) {
	voltarCard = voltar;
 	$('.nav-tab a[href="#gerenciar"]').tab('show');
 	$('.nav-tab a[href="#adicionareditar"]').tab('show');		
}
