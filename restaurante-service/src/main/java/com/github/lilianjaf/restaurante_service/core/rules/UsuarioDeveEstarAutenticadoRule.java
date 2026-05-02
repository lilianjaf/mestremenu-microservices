package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.BuscarCardapioPorRestauranteRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.BuscarItemCardapioPorIdRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioNaoAutenticadoException;

public class UsuarioDeveEstarAutenticadoRule implements ValidadorCriacaoRestauranteRule, AtualizarRestauranteRule, InativarRestauranteRule, ListarRestaurantesRule, BuscarRestauranteRule {
    @Override
    public void validar(CriacaoRestauranteContext context) {
        validarAutenticacao(context);
    }

    @Override
    public void validar(AtualizarRestauranteRuleContextDto context) {
        validarAutenticacao(context);
    }

    @Override
    public void validar(InativacaoRestauranteContext context) {
        validarAutenticacao(context);
    }

    @Override
    public void validar(ListarRestaurantesRuleContextDto context) {
        validarAutenticacao(context);
    }

    @Override
    public void validar(BuscarRestauranteRuleContextDto context) {
        validarAutenticacao(context);
    }

    public void validarAutenticacao(RestauranteContextBase context) {
        if (!context.isUsuarioAutenticado()) {
            lancarExcecao();
        }
    }

    private void lancarExcecao() {
        throw new UsuarioNaoAutenticadoException("Usuário não autenticado");
    }

    public static ValidadorPermissaoCardapioRule<BuscarItemCardapioPorIdRuleContextDto> paraBuscarItem() {
        return new UsuarioDeveEstarAutenticadoRule()::validarAutenticacao;
    }

    public static ValidadorPermissaoCardapioRule<BuscarCardapioPorRestauranteRuleContextDto> paraBuscarCardapio() {
        return new UsuarioDeveEstarAutenticadoRule()::validarAutenticacao;
    }
}
