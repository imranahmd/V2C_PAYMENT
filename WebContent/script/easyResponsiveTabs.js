// Easy Responsive Tabs Plugin
// Author: Samson.Onna <Email : samson3d@gmail.com>
(function ($) {
    $.fn.extend({
        easyResponsiveTabs: function (options) {
            //Set the default values, use comma to separate the settings, example:
            var defaults = {
                type: 'default', //default, vertical, accordion;
                width: 'auto',
                fit: true
            }
            //Variables
            var options = $.extend(defaults, options);            
            var opt = options, jtype = opt.type, jfit = opt.fit, jwidth = opt.width, vtabs = 'vertical', accord = 'accordion';

            //Main function
            this.each(function () {
                var $respTabs = $(this);
                $respTabs.find('ul.resp-tabs-list li').addClass('resp-tab-item');
                $respTabs.css({
                    'display': 'block',
                    'width': jwidth
                });

                $respTabs.find('.resp-tabs-container > div').addClass('resp-tab-content');
                jtab_options();
                //Properties Function
                function jtab_options() {
                    if (jtype == vtabs) {
                        $respTabs.addClass('resp-vtabs');
                        $respTabs.addClass('resp-vtabs');
                    }
                    if (jfit == true) {
                        $respTabs.css({ width: '100%', margin: '0px' });
                    }
                    if (jtype == accord) {
                        $respTabs.addClass('resp-easy-accordion');
                        $respTabs.find('.resp-tabs-list').css('display', 'none');
                    }
                }

                //Assigning the h2 markup
                var $tabItemh2;
                $respTabs.find('.resp-tab-content').before("<h2 class='resp-accordion' role='tab'><span class='resp-arrow'></span></h2>");

                var itemCount = 0;
                $respTabs.find('.resp-accordion').each(function () {
                    $tabItemh2 = $(this);
                    var innertext = $respTabs.find('.resp-tab-item:eq(' + itemCount + ')').text();
                    $respTabs.find('.resp-accordion:eq(' + itemCount + ')').append(innertext);
                   // $tabItemh2.attr('aria-controls', 'tab_item-' + (itemCount));
                    itemCount++;
                });

                //Assigning the 'aria-controls' to Tab items
                var count = 0,
                    $tabContent;
                    $respTabs.find('.resp-tab-item').each(function () {
                    $tabItem = $(this);
                   // $tabItem.attr('aria-controls', 'tab_item-' + (count));
                    $tabItem.attr('role', 'tab');

                   
                    //First active tab                   
                    $respTabs.find('.resp-tab-item').first().addClass('resp-tab-active');
                    $respTabs.find('.resp-accordion').first().addClass('resp-tab-active');
                    $respTabs.find('.resp-tab-content').first().addClass('resp-tab-content-active').attr('style', 'display:none');

                    //Assigning the 'aria-labelledby' attr to tab-content
                    var tabcount = 0;
                    $respTabs.find('.resp-tab-content').each(function () {
                        $tabContent = $(this);
                        //$tabContent.attr('aria-labelledby', 'tab_item-' + (tabcount));
                        tabcount++;
                    });
                    count++;
                });
                
                    //                  
                var indexed = $( 'li' ).index( this );
                

                //Tab Click action function
                $respTabs.find("[role=tab]").each(function () {
                    var $currentTab = $(this);
                    $currentTab.click(function () {
                    //console.log("This is my tab"+ indexed);
                        var $tabAria = $currentTab.attr('aria-controls');

                        if ($currentTab.hasClass('resp-accordion') && $currentTab.hasClass('resp-tab-active')) {
                            $respTabs.find('.resp-tab-content-active').slideUp('', function () { $(this).addClass('resp-accordion-closed'); });
                            $currentTab.removeClass('resp-tab-active');
                            return false;
                        }
                        if (!$currentTab.hasClass('resp-tab-active') && $currentTab.hasClass('resp-accordion')) {
                            $respTabs.find('.resp-tab-active').removeClass('resp-tab-active');
                            $respTabs.find('.resp-tab-content-active').slideUp().removeClass('resp-tab-content-active resp-accordion-closed');
                            $respTabs.find("[aria-controls=" + $tabAria + "]").addClass('resp-tab-active');
                            $respTabs.find('.resp-tab-content[aria-labelledby = ' + $tabAria + ']').slideDown().addClass('resp-tab-content-active');
                        } else {
                            $respTabs.find('.resp-tab-active').removeClass('resp-tab-active');
                            $respTabs.find('.resp-tab-content-active').removeAttr('style').removeClass('resp-tab-content-active').removeClass('resp-accordion-closed');
                            $respTabs.find("[aria-controls=" + $tabAria + "]").addClass('resp-tab-active');
//       Tabs                    $respTabs.find("[aria-controls=" + $tabAria + "]").addClass('red-border');
                            $respTabs.find('.resp-tab-content[aria-labelledby = ' + $tabAria + ']').addClass('resp-tab-content-active').attr('style', 'display:block');
//      Content                      $respTabs.find('.resp-tab-content[aria-labelledby = ' + $tabAria + ']').addClass('red-border');
                            
                        }
                    });
                    //Window resize function                   
                    $(window).resize(function () {
                        $respTabs.find('.resp-accordion-closed').removeAttr('style');
                    });
					if(creditCardVisibility == true){
						$('#cr-section').removeClass('.resp-tab-content');
					    $('#cr-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio1").checked = true;
						$('.visible').hide();
						$('#tab').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == true){
						$('#dr-section').removeClass('.resp-tab-content');
					    $('#dr-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio2").checked = true;
						$('#tab1').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == true){
						$('#net-section').removeClass('.resp-tab-content');
					    $('#net-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio3").checked = true;
						$('#tab2').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == false && walletVisibility == false && bqrVisibility == true){
						$('#bqr-content').removeClass('.resp-tab-content');
					    $('#bqr-content').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio7").checked = true;
						$('#tab3').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == false && walletVisibility == false && bqrVisibility == false && upiVisibility == true){
						$('#upi-section').removeClass('.resp-tab-content');
					    $('#upi-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio4").checked = true;
						$('#tab4').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == false && walletVisibility == false && bqrVisibility == false && upiVisibility == false && atomUPIVisibility == true){
						$('#atom-upi-sectionn').removeClass('.resp-tab-content');
					    $('#atom-upi-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio5").checked = true;
						$('#tab5').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == false && walletVisibility == false && bqrVisibility == false && upiVisibility == false && atomUPIVisibility == false && walletVisibility == true){
						$('#wallet-section').removeClass('.resp-tab-content');
					    $('#wallet-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio6").checked = true;
						$('#tab6').addClass('resp-tab-active');
					}
					else if(creditCardVisibility == false && debitCardVisibility == false && netBankVisibility == false && walletVisibility == false && bqrVisibility == false && upiVisibility == false && atomUPIVisibility == false && walletVisibility == false && walletVisibility == true){
						$('#emi-section').removeClass('.resp-tab-content');
					    $('#emi-section').addClass('resp-tab-content-active').attr('style', 'display:block');
						document.getElementById("radio7").checked = true;
						$('#tab7').addClass('resp-tab-active');
					}
					
                });
            });
        }
    });
})(jQuery);

