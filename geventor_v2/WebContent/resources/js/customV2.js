$(document).ready(function () {
    console.log("document is ready");
    $('[data-toggle="offcanvas"], #navToggle').on('click', function () {
        $('.offcanvas-collapse').toggleClass('open')
    })
});
window.onload = function () {
    console.log("window is loaded");
};

// tab nav
const tabButtons = document.querySelectorAll('.tab-button');
const tabPanes = document.querySelectorAll('.tab-pane');

tabButtons.forEach(button => {
  button.addEventListener('click', () => {
    const target = button.dataset.target;
    
    tabButtons.forEach(b => {
      b.classList.remove('active');
    });
    
    tabPanes.forEach(pane => {
      pane.classList.remove('active');
    });
    
    button.classList.add('active');
    document.getElementById(target).classList.add('active');
  });
});

function selAba(aba) {
 	$('.nav-tab a[href="#'+ aba + '"]').tab('show');		
}

function openModal(modal) {
	$('#' + modal + '').modal('show');
}

function closeModal(modal) {
	$('#' + modal + '').modal('hide');
}

// seleciona todos os elementos .nav-tab-link
var tabs = document.querySelectorAll('.nav-tab-link');

// adiciona um ouvinte de eventos de clique a cada tab
tabs.forEach(function(tab) {
 tab.addEventListener('click', function(e) {
   e.preventDefault();

   // remove a classe 'active' de todas as tabs e tab-panes
   var activeTabs = document.querySelectorAll('.nav-link.active');
   activeTabs.forEach(function(activeTab) {
     activeTab.classList.remove('active');
   });
   var activePanes = document.querySelectorAll('.tab-pane.active');
   activePanes.forEach(function(activePane) {
     activePane.classList.remove('active');
   });

   // adiciona a classe 'active' à tab e pane clicadas
   var tabClicked = e.target;
   var tabId = tabClicked.getAttribute('href');
   var pane = document.querySelector(tabId);
   tabClicked.classList.add('active');
   pane.classList.add('active');
 });
});